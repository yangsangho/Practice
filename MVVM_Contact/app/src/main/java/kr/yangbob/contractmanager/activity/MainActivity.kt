package kr.yangbob.contractmanager.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.PopupWindow
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.sort_menu.view.*
import kr.yangbob.contractmanager.R
import kr.yangbob.contractmanager.db.Contact
import kr.yangbob.contractmanager.recycler.ContactAdapter
import kr.yangbob.contractmanager.viewmodel.ContactViewModel
import kr.yangbob.contractmanager.viewmodel.Sort

class MainActivity : AppCompatActivity()
{
    private lateinit var contactViewModel: ContactViewModel
    private lateinit var liveContacts: LiveData<List<Contact>>
    
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        contactViewModel = ViewModelProviders.of(this).get(ContactViewModel::class.java)
        liveContacts = contactViewModel.getAll()
        
        val contactAdapter = ContactAdapter(
                { contact: Contact? ->
                    val intent = Intent(this, AddActivity::class.java).apply {
                        putExtra(AddActivity.EXTRA_CONTACT_NAME, contact?.name)
                        putExtra(AddActivity.EXTRA_CONTACT_NUMBER, contact?.number)
                        putExtra(AddActivity.EXTRA_CONTACT_ID, contact?.id)
                    }
                    startActivity(intent)
                },
                { contact: Contact? -> deleteDialog(contact) })
        contactAdapter.setContacts(contactViewModel.getSortedList())
        
        addFab.setOnClickListener {
            startActivity(Intent(this, AddActivity::class.java))
        }
        
        val lm = LinearLayoutManager(this)
        recyclerView.adapter = contactAdapter
        recyclerView.layoutManager = lm
        recyclerView.setHasFixedSize(true)
        
        liveContacts.observe(this, Observer<List<Contact>> {
            contactAdapter.setContacts(contactViewModel.getSortedList())
        })
        
        val popupView = layoutInflater.inflate(R.layout.sort_menu, null)
        contactViewModel.sortNameIcon.observe(this, Observer {
            Log.i("TEST", "Name Icon = $it")
            if (it == null) popupView.sortNameIcon.visibility = View.INVISIBLE
            else
            {
                popupView.sortNameIcon.visibility = View.VISIBLE
                popupView.sortNameIcon.setImageResource(it)
            }
        })
        contactViewModel.sortNumberIcon.observe(this, Observer {
            Log.i("TEST", "Number Icon = $it")
            if (it == null) popupView.sortNumberIcon.visibility = View.INVISIBLE
            else
            {
                popupView.sortNumberIcon.visibility = View.VISIBLE
                popupView.sortNumberIcon.setImageResource(it)
            }
        })
        popupView.cvName.setOnClickListener {
            contactViewModel.setSortIcon(Sort.NAME)
            contactAdapter.setContacts(contactViewModel.getSortedList())
        }
        popupView.cvNumber.setOnClickListener {
            contactViewModel.setSortIcon(Sort.NUMBER)
            contactAdapter.setContacts(contactViewModel.getSortedList())
        }
        val popupWindow = PopupWindow(popupView, ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT)
        popupWindow.isFocusable = true
        ibSort?.setOnClickListener {
            popupWindow.showAtLocation(layoutMain, Gravity.TOP + Gravity.START, it.x.toInt(), it.y.toInt() + it.height * 2)
        }
    }
    
    private fun deleteDialog(contact: Contact?)
    {
        if (contact == null) return
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Delete selected contact?").setNegativeButton("NO") { _, _ -> }
                .setPositiveButton("YES") { _, _ ->
                    contactViewModel.delete(contact)
                }
        builder.show()
    }
}