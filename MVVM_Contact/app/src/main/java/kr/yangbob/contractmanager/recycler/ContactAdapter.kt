package kr.yangbob.contractmanager.recycler

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kr.yangbob.contractmanager.R
import kr.yangbob.contractmanager.db.Contact

class ContactAdapter(val contactClick: (Contact?) -> Unit,
                     val contactLongClick: (Contact?) -> Unit): RecyclerView.Adapter<ContactAdapter.ContactViewHolder>()
{
    private var contacts: List<Contact>? = listOf()
    inner class ContactViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
    {
        private val tvInitial = itemView.findViewById<TextView>(R.id.item_tvInitial)
        private val tvName = itemView.findViewById<TextView>(R.id.item_tvName)
        private val tvNumber = itemView.findViewById<TextView>(R.id.item_tvNumber)

        fun bind(contact: Contact?)
        {
            tvInitial.text = contact?.initial.toString()
            tvName.text = contact?.name
            tvNumber.text = contact?.number

            itemView.setOnClickListener {
                contactClick(contact)
            }
            itemView.setOnLongClickListener {
                contactLongClick(contact)
                true
            }
        }
    }

    fun ViewGroup.inflate(context: Context, resourceId: Int) =
            LayoutInflater.from(context).inflate(resourceId, this, false)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder
    {
        return ContactViewHolder(parent.inflate(parent.context,
                                                R.layout.item_contact))
    }

    override fun getItemCount(): Int
    {
        return contacts?.size ?: 0
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int)
    {
        holder.bind(contacts?.get(position))
    }

    fun setContacts(contacts: List<Contact>?)
    {
        this.contacts = contacts
        notifyDataSetChanged()
    }
}