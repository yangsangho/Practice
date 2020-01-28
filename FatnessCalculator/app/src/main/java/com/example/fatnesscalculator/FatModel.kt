package com.example.fatnesscalculator

import android.app.Application
import androidx.core.content.edit
import androidx.preference.PreferenceManager

class FatModel(application: Application)
{
    private val pref = PreferenceManager.getDefaultSharedPreferences(application)
    fun saveData(appearanceInfo: AppearanceInfo)
    {
        pref.edit(commit = true) {
            putString(KEY_HEIGHT, appearanceInfo.height)
            putString(KEY_WEIGHT, appearanceInfo.weight)
        }
    }

    fun loadData() : AppearanceInfo?
    {
        val height = pref.getString(KEY_HEIGHT, null)
        val weight = pref.getString(KEY_WEIGHT, null)
        if(height.isNullOrEmpty() || weight.isNullOrEmpty()) return null
        return AppearanceInfo(height, weight)
    }
}