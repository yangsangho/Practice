package kr.yangbob.contractmanager.viewmodel

import android.app.Application
import android.content.Intent
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kr.yangbob.contractmanager.R
import kr.yangbob.contractmanager.activity.AddActivity
import kr.yangbob.contractmanager.db.Contact
import kr.yangbob.contractmanager.repo.ContactRepository

enum class Sort{NAME, NUMBER}

class ContactViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = ContactRepository(application)
    private val contacts = repository.getAll()
    val addName = MutableLiveData<String>()
    val addNumber = MutableLiveData<String>()
    var id: Long? = null
    
    private var sort: Sort
    private var isAsc: Boolean
    val sortNameIcon = MutableLiveData<Int>()
    val sortNumberIcon = MutableLiveData<Int>()

    init {
        resetAddInfo()
        sort = Sort.NAME
        isAsc = true
        sortNameIcon.value = R.drawable.ic_arrow_upward_black_16dp
        sortNumberIcon.value = null
    }

    fun getAll(): LiveData<List<Contact>> {
        return this.contacts
    }

    private fun insert(contact: Contact) {
        repository.insert(contact)
    }

    fun delete(contact: Contact) {
        repository.delete(contact)
    }

    fun setAddInfo(intent: Intent)
    {
        addName.value = intent.getStringExtra(AddActivity.EXTRA_CONTACT_NAME)
        addNumber.value = intent.getStringExtra(AddActivity.EXTRA_CONTACT_NUMBER)
        id = intent.getLongExtra(AddActivity.EXTRA_CONTACT_ID, -1)
    }
    fun resetAddInfo()
    {
        addName.value = ""
        addNumber.value = ""
        id = null
    }
    fun chkEmptyAddInfo(): Boolean = addName.value!!.isEmpty() || addNumber.value!!.isEmpty()
    fun insertContact()
    {
        val initial = addName.value!![0].toUpperCase()
        val contact = Contact(id, addName.value!!, addNumber.value!!, initial)
        Log.i("TEST", "${contact.hashCode()}")
        insert(contact)
        resetAddInfo()
    }
    
    fun getSortedList(): List<Contact>? = contacts.value.let { raw ->
        when (sort) {
            Sort.NAME -> if (isAsc) raw?.sortedBy { it.name } else raw?.sortedByDescending { it.name }
            Sort.NUMBER -> if (isAsc) raw?.sortedBy { it.number } else raw?.sortedByDescending { it.number }
        }
    }
    
    fun setSortIcon(sort: Sort)
    {
        if(this.sort != sort)
        {
            this.sort = sort
            isAsc = true
            sortNumberIcon.value = if(sort == Sort.NUMBER) R.drawable.ic_arrow_upward_black_16dp else null
            sortNameIcon.value = if(sort == Sort.NAME) R.drawable.ic_arrow_upward_black_16dp else null
        }
        else
        {
            isAsc = !isAsc
            if(sortNameIcon.value != null) sortNameIcon.value = if(isAsc) R.drawable.ic_arrow_upward_black_16dp else R.drawable.ic_arrow_downward_black_16dp
            if(sortNumberIcon.value != null) sortNumberIcon.value = if(isAsc) R.drawable.ic_arrow_upward_black_16dp else R.drawable.ic_arrow_downward_black_16dp
        }
        Log.i("TEST", "sort = $sort, isAsc = $isAsc, name.value = ${sortNameIcon.value}, num.value = ${sortNumberIcon.value}")
    }
}