package com.yangbob.flashlight

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity: AppCompatActivity()
{

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.i("TEST", "왜 안되냐")
        swFlash.setOnCheckedChangeListener { _, isChecked ->
            Log.i("TEST", "setOnCheckedChangeListener()!")
            if(isChecked) startService(Intent(this, TorchService::class.java).apply { action = "on" })
            else startService(Intent(this, TorchService::class.java).apply { action = "off" })
        }
    }
}
