package com.yangbob.flashlight

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

class TorchService: Service()
{
    private val torch: Torch by lazy {
        Torch(this)
    }
    private var isRunning = false

    override fun onCreate()
    {
        super.onCreate()
        Log.i("TEST", "서비스 생성. onCreate()")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int
    {
        Log.i("TEST", "서비스 시작. onStartCommand()")
        when(intent?.action)
        {
            "on" -> {
                torch.flashOn()
                isRunning = true
            }
            "off" -> {
                torch.flashOff()
                isRunning = false
            }
            else -> {
                isRunning = !isRunning
                if(isRunning) torch.flashOn()
                else torch.flashOff()
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy()
    {
        super.onDestroy()
        Log.i("TEST", "서비스 종료. onDestroy()")
    }

    override fun onBind(intent: Intent): IBinder
    {
        TODO("Return the communication channel to the service.")
    }
}
