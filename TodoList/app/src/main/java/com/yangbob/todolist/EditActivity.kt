package com.yangbob.todolist

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_edit.*
import java.util.*

class EditActivity: AppCompatActivity()
{
    var realm = Realm.getDefaultInstance()
    var calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        var id = intent.getLongExtra("id", -1L)
        if(id == -1L) insertMode()
        else updateMode(id)

        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            calendar.set(year, month, dayOfMonth)
        }
    }

    private fun insertMode()
    {
        deleteFab.hide()
        doneFab.setOnClickListener {
            insertTodo()
        }
    }

    private fun updateMode(id: Long)
    {
        val todo = realm.where<Todo>().equalTo("id", id).findFirst()!!
        todoEditText.setText(todo.title)
        calendarView.date = todo.date
        doneFab.setOnClickListener {
            updateTodo(id)
        }
        deleteFab.setOnClickListener {
            deleteTodo(id)
        }
    }

    override fun onDestroy()
    {
        super.onDestroy()
        realm.close()
    }

    private fun insertTodo()
    {
        realm.executeTransaction {
            val newItem = it.createObject<Todo>(nextId())   // insert into todo(id) values(nextId())
            newItem.title = todoEditText.text.toString()
            newItem.date = calendar.timeInMillis
        }
        AlertDialog.Builder(this).setMessage("내용이 추가되었습니다.")
            .setPositiveButton("OK") { _, _ -> finish() }.create().show()
    }

    private fun nextId(): Int
    {
        val maxId = realm.where<Todo>().max("id")           // select max(id) from todo
        if(maxId != null) return maxId.toInt() + 1
        return 0
    }

    private fun updateTodo(id: Long)
    {
        realm.executeTransaction {
            val updateItem = it.where<Todo>().equalTo("id", id).findFirst()!!
            updateItem.title = todoEditText.text.toString()
            updateItem.date = calendar.timeInMillis
        }
        AlertDialog.Builder(this).setMessage("내용이 수정되었습니다.")
            .setPositiveButton("OK") { _, _ -> finish() }.create().show()
    }

    private fun deleteTodo(id: Long)
    {
        realm.executeTransaction {
            val deleteItem = it.where<Todo>().equalTo("id", id).findFirst()!!
            deleteItem.deleteFromRealm()
        }
        AlertDialog.Builder(this).setMessage("내용이 삭제되었습니다.")
            .setPositiveButton("OK") { _, _ -> finish() }.create().show()
    }

}
