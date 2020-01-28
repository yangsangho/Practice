package com.example.fatnesscalculator

import android.widget.ImageView
import androidx.databinding.BindingAdapter

object FatAdapters {
    @BindingAdapter("app:resultIcon")
    @JvmStatic fun resultIcon(view: ImageView, resourceId: Int) {
        view.setImageResource(resourceId)
    }
}