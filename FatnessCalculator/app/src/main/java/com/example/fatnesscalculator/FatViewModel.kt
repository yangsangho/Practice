package com.example.fatnesscalculator

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import kotlin.math.pow

data class AppearanceInfo(val height: String, val weight: String)

class FatViewModel(application: Application) : AndroidViewModel(application)
{
    private val repository = FatModel(application)
    val height = MutableLiveData<String>()
    val weight = MutableLiveData<String>()
    val resultIcon = MutableLiveData<Int>()
    val resultText = MutableLiveData<String>()
    
    init
    {
        val appearanceInfo = repository.loadData()
        height.value = appearanceInfo?.height ?: ""
        weight.value = appearanceInfo?.weight ?: ""
    }
    
    fun saveData() : Boolean
    {
        if(height.value.isNullOrEmpty() || weight.value.isNullOrEmpty()) return false
        repository.saveData( AppearanceInfo(height.value!!, weight.value!!) )
        return true
    }

    fun getResult() : Double
    {
        if(height.value.isNullOrEmpty() || weight.value.isNullOrEmpty()) return 0.0
        val bmi = weight.value!!.toInt() / (height.value!!.toInt() / 100.0).pow(2)

        resultText.value = when
        {
            bmi >= 35 -> "고도 비만"
            bmi >= 30 -> "2단계 비만"
            bmi >= 25 -> "1단계 비만"
            bmi >= 23 -> "과체중"
            bmi >= 18.5 -> "정상"
            else -> "저체중"
        }

        resultIcon.value = when
        {
            bmi >= 23 -> R.drawable.ic_sentiment_very_dissatisfied_black_24dp
            bmi >= 18.5 -> R.drawable.ic_sentiment_satisfied_black_24dp
            else -> R.drawable.ic_sentiment_dissatisfied_black_24dp
        }

        return bmi
    }
}