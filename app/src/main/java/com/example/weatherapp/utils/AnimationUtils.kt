package com.example.weatherapp.utils

import android.view.View

object AnimationUtils {

    fun scaleOption(item: View?, scale: Float) {
        item?.animate()?.scaleX(scale)?.scaleY(scale)?.setDuration(0)?.start()
    }

    fun fadeOption(item: View?, fade: Float) {
        item?.animate()?.setDuration(0)?.alpha(fade)?.start()
    }
}