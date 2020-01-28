package com.example.fatnesscalculator

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.example.fatnesscalculator.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

const val KEY_HEIGHT = "KEY_HEIGHT"
const val KEY_WEIGHT = "KEY_WEIGHT"

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: FatViewModel
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        viewModel = ViewModelProviders.of(this).get(FatViewModel::class.java)
        binding.viewModel = viewModel
        
//        viewModel.loadData()

        setEvent()
    }

    private fun setEvent()
    {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        btnResult.setOnClickListener {
            imm.hideSoftInputFromWindow(etHeight.windowToken, 0)
            imm.hideSoftInputFromWindow(etWeight.windowToken, 0)
            if( !viewModel.saveData() ) Snackbar.make(layoutMain, "키와 몸무게를 입력해주세요.", Snackbar.LENGTH_LONG).show()
            else startActivity( Intent(this, ResultActivity::class.java) )
        }
    }
}
