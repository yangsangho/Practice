package com.yangbob.todolist

import android.content.Context
import android.content.Intent
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.realm.RealmResults

class MyAdapter(val context: Context, val list: RealmResults<Todo>): RecyclerView.Adapter<MyAdapter.MyViewHolder>()
{
    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
    {
        val tvDate = itemView.findViewById<TextView>(R.id.tvDate)
        val tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)

        fun bind(todo: Todo)
        {
            tvDate.setText(DateFormat.format("yyyy/MM/dd", todo.date))
            tvTitle.setText(todo.title)
            itemView.setOnClickListener {
                val intent = Intent(context, EditActivity::class.java).apply {
                    putExtra("id", todo.id)
                }
                context.startActivity(intent)
            }
        }

    }

    fun ViewGroup.inflate(resId: Int, attach: Boolean = false): View = LayoutInflater.from(context).inflate(resId, this, attach)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder
    {
        return MyViewHolder(parent.inflate(R.layout.item_todo))
    }

    override fun getItemCount(): Int
    {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int)
    {
        list.get(position)?.let {
            holder.bind(it)
        }
    }
}