package com.example.weatherapp.adapters

import android.text.TextWatcher
import android.widget.EditText

object Extentions {

    fun EditText.applyWithDisabledTextWatcher(textWatcher: TextWatcher, codeBlock: EditText.() -> Unit) {
        this.removeTextChangedListener(textWatcher)
        codeBlock()
        this.addTextChangedListener(textWatcher)
    }

}
