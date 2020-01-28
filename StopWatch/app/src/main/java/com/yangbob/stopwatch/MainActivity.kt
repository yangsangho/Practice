package com.yangbob.stopwatch

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.yangbob.stopwatch.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: MyViewModel
    lateinit var lapTimes: LiveData<List<LapData>>
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(MyViewModel::class.java)
        lapTimes = viewModel.lapObserver
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.viewmodel = viewModel
    
        val listAdapter = ListAdapter(lapTimes.value!!)
        val lm = LinearLayoutManager(this)
        recyclerView.adapter = listAdapter
        recyclerView.layoutManager = lm
        recyclerView.setHasFixedSize(true)
        
        lapTimes.observe(this, Observer {
            listAdapter.notifyDataSetChanged()
        })
    }
}
