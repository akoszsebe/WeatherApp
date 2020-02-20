package com.example.weatherapp.utils

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import com.example.weatherapp.R

class LoaderDialog {

    companion object {

        fun buildLoader(context: Context): Dialog {
            val inflator =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = inflator.inflate(R.layout.progress_bar, null)
            var dialog = Dialog(context, R.style.LoaderDialogTheme)
            dialog.setContentView(view)
            dialog.setCancelable(false)
            return dialog
        }
    }
}