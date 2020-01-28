package kr.yangbob.contractmanager.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_add.*
import kr.yangbob.contractmanager.R
import kr.yangbob.contractmanager.databinding.ActivityAddBinding
import kr.yangbob.contractmanager.viewmodel.ContactViewModel

class AddActivity : AppCompatActivity()
{
    private lateinit var viewModel: ContactViewModel
    
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        val binding: ActivityAddBinding = DataBindingUtil.setContentView(this, R.layout.activity_add)
        
        viewModel = ViewModelProviders.of(this).get(ContactViewModel::class.java)
        binding.viewmodel = viewModel
        
        if (intent != null && intent.hasExtra(EXTRA_CONTACT_NAME) && intent.hasExtra(EXTRA_CONTACT_NUMBER)) viewModel.setAddInfo(intent)
        btnDone.setOnClickListener {
            if (viewModel.chkEmptyAddInfo()) Toast.makeText(this, "Please enter name and number", Toast.LENGTH_LONG).show()
            else
            {
                viewModel.insertContact()
                finish()
            }
        }
    }
    
    companion object
    {
        const val EXTRA_CONTACT_NAME = "EXTRA_CONTACT_NAME"
        const val EXTRA_CONTACT_NUMBER = "EXTRA_CONTACT_NUMBER"
        const val EXTRA_CONTACT_ID = "EXTRA_CONTACT_ID"
    }
}