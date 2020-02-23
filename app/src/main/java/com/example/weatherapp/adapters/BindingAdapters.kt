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
import com.example.weatherapp.utils.Extensions.of
import com.example.weatherapp.utils.UnitOfMeasurement
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

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
    @BindingAdapter("isFavorite")
    fun bindIsFavorite(view: ImageView, isFavorite: Boolean) {
        if (isFavorite) {
            view.setImageResource(R.drawable.ic_star_fill)
        } else {
            view.setImageResource(R.drawable.ic_star_unfill)
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
        val formatter = SimpleDateFormat(dateFormat,Locale.getDefault())
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = long * 1000
        this.text =  formatter.format(calendar.time)
    }

    @JvmStatic
    @BindingAdapter("setTextUnitConverted", "setTemperatureUnit")
    fun TextView.setTextUnitConverted(temp : Double, unitOfMeasurement: UnitOfMeasurement){
        this.text =  temp.of(unitOfMeasurement).roundToInt().toString().of(unitOfMeasurement)
    }

    @JvmStatic
    @BindingAdapter("setTextUnitConverted1","setTextUnitConverted2", "setTemperatureUnit")
    fun TextView.setTextUnitConvertedMultiple(temp1 : Double, temp2 : Double, unitOfMeasurement: UnitOfMeasurement){
        this.text =  this.context.getString(R.string.minmaxtemptemplate,temp1.of(unitOfMeasurement), temp1.of(unitOfMeasurement))
    }

}


class DrawableAlwaysCrossFadeFactory : TransitionFactory<Drawable> {
    private val resourceTransition: DrawableCrossFadeTransition = DrawableCrossFadeTransition(300, true) //customize to your own needs or apply a builder pattern
    override fun build(dataSource: DataSource?, isFirstResource: Boolean): Transition<Drawable> {
        return resourceTransition
    }
}