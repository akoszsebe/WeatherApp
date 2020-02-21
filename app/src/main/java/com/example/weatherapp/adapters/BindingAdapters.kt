package com.example.weatherapp.adapters

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.transition.DrawableCrossFadeTransition
import com.bumptech.glide.request.transition.Transition
import com.bumptech.glide.request.transition.TransitionFactory
import com.example.weatherapp.R
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
            .load("https://openweathermap.org/img/wn/$imageName@2x.png")
            .apply(RequestOptions().circleCrop())
            .transition(DrawableTransitionOptions.with(DrawableAlwaysCrossFadeFactory()))
            .error(R.drawable.ic_warning)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
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


class DrawableAlwaysCrossFadeFactory : TransitionFactory<Drawable> {
    private val resourceTransition: DrawableCrossFadeTransition = DrawableCrossFadeTransition(300, true) //customize to your own needs or apply a builder pattern
    override fun build(dataSource: DataSource?, isFirstResource: Boolean): Transition<Drawable> {
        return resourceTransition
    }
}