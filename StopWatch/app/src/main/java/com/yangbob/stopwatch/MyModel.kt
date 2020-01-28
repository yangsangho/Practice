package com.yangbob.stopwatch

import androidx.lifecycle.MutableLiveData
import java.util.*
import kotlin.concurrent.timer

class MyModel
{
    private var time = 0
    private var timerTask : Timer? = null
    private var isRunning = false
    private var lap = 1

    val seconds = MutableLiveData<Int>()
    val millis = MutableLiveData<Int>()

    init {
        seconds.value = 0
        millis.value = 0
    }

    fun onClickStartPause()
    {
        isRunning = !isRunning

        if(isRunning)
        {
            timerTask = timer(period = 10){
                time++
                seconds.postValue(time / 100)
                millis.postValue(time % 100)
            }
        }
        else timerTask?.cancel()
    }

    fun onClickReset()
    {
        if(isRunning)
        {
            timerTask?.cancel()
            isRunning = false
        }
        time = 0
        lap = 1
        seconds.value = 0
        millis.value = 0
    }
    
    fun getLap() : LapData
    {
        val lap = MutableLiveData<Int>()
        lap.value = this.lap
        this.lap++
        val time = MutableLiveData<String>()
        time.value = "${seconds.value}.${millis.value}"
        
        return LapData(lap, time)
    }
    
    fun isRunning() : Boolean = isRunning
}