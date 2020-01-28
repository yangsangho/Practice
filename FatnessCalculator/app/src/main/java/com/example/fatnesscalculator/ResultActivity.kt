package com.example.fatnesscalculator

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.example.fatnesscalculator.databinding.ActivityResultBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_result.*

class ResultActivity : AppCompatActivity() {

    private lateinit var viewModel: FatViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityResultBinding = DataBindingUtil.setContentView(this, R.layout.activity_result)
        binding.lifecycleOwner = this
        viewModel = ViewModelProviders.of(this).get(FatViewModel::class.java)
        binding.viewModel = viewModel

        val bmi = viewModel.getResult()
        Snackbar.make(layoutMain, "$bmi", Snackbar.LENGTH_LONG).show()
    }
}
