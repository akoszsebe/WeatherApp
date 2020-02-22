package com.example.weatherapp.adapters

import android.text.TextWatcher
import android.text.format.DateUtils
import android.widget.EditText


object Extentions {

    fun EditText.applyWithDisabledTextWatcher(textWatcher: TextWatcher, codeBlock: EditText.() -> Unit) {
        this.removeTextChangedListener(textWatcher)
        codeBlock()
        this.addTextChangedListener(textWatcher)
    }

    fun isTomorrow(timeInMilliseconds : Long): Boolean {
        return DateUtils.isToday(timeInMilliseconds - DateUtils.DAY_IN_MILLIS)
    }
}
