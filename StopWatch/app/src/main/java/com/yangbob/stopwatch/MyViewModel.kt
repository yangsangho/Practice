package com.yangbob.stopwatch

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyViewModel : ViewModel()
{
    private val model = MyModel()
    val seconds: LiveData<Int> = model.seconds
    val millis: LiveData<Int> = model.millis
    val fabIconResourceId = MutableLiveData<Int>()
    
    val lapObserver = MutableLiveData<List<LapData>>()
    val lapTimes = ArrayList<LapData>() // 따로 분리해서 매번 set해줘야 observe 가능
    
    init
    {
        fabIconResourceId.value = R.drawable.ic_play_arrow_black_24dp
        lapObserver.value = lapTimes
    }
    
    fun onClickStartPause(view: View)
    {
        model.onClickStartPause()
        if(model.isRunning()) fabIconResourceId.value = R.drawable.ic_pause_black_24dp
        else fabIconResourceId.value = R.drawable.ic_play_arrow_black_24dp
    }

    fun onClickReset(view: View)
    {
        fabIconResourceId.value = R.drawable.ic_play_arrow_black_24dp
        lapTimes.clear()
        lapObserver.value = lapTimes    // notify
        model.onClickReset()
    }
    
    fun onClickLap(view: View)
    {
        if(!model.isRunning()) return
        val lapData = model.getLap()
        lapTimes.add(lapData)
        lapObserver.value = lapTimes    // notify
    }
}