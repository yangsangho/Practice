package com.yangbob.stopwatch

import androidx.lifecycle.LiveData

data class LapData(
        val lap: LiveData<Int>,
        val time: LiveData<String>)