package com.yangbob.todolist

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import io.realm.Realm
import io.realm.Sort
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity: AppCompatActivity()
{
    var realm = Realm.getDefaultInstance()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        addFab.setOnClickListener {
            startActivity( Intent(this, EditActivity::class.java) )                     // insert니까 id없이 intent 전달
        }

        val realmResult = realm.where<Todo>().findAll().sort("date", Sort.DESCENDING)
        val adapter = MyAdapter(this, realmResult)
        recyclerView.adapter = adapter

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)

        realmResult.addChangeListener { _ -> adapter.notifyDataSetChanged() }
    }

    override fun onDestroy()
    {
        super.onDestroy()
        realm.close()
    }
}
