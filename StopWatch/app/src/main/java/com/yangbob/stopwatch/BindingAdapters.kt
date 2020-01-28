package com.yangbob.stopwatch

import androidx.databinding.BindingAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

object BindingAdapters
{
    @BindingAdapter("app:fabIcon")
    @JvmStatic fun fabIcon(view: FloatingActionButton, resourceId: Int) {
        view.setImageResource(resourceId)
    }
}