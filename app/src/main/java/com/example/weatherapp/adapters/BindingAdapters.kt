package com.example.weatherapp.adapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.text.SimpleDateFormat
import java.util.*

object BindingAdapters {

    @JvmStatic
    @BindingAdapter("isGone")
    fun bindIsGone(view: View, isGone: Boolean) {
        view.visibility = if (isGone) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    @JvmStatic
    @BindingAdapter("weatherImage")
    fun ImageView.loadImage(imageName: String?) {
        Glide.with(this)
            .load("https://openweathermap.org/img/wn/$imageName.png")
            .apply(RequestOptions().circleCrop())
            .into(this)
    }

    @JvmStatic
    @BindingAdapter("setDateFromSeconds", "dateFormat")
    fun TextView.setDateFromSeconds(long : Long, dateFormat: String){
        val formatter = SimpleDateFormat(dateFormat)
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = long * 1000
        this.text =  formatter.format(calendar.getTime())
    }

}