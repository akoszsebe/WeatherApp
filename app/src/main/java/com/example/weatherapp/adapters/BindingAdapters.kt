package com.example.weatherapp.adapters

import android.view.View
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import android.widget.ImageView;


@BindingAdapter("isGone")
fun bindIsGone(view: View, isGone: Boolean) {
    view.visibility = if (isGone) {
        View.GONE
    } else {
        View.VISIBLE
    }
}

@BindingAdapter("weatherImage")
fun ImageView.loadImage(imageName: String?) {
    Glide.with(this)
        .load("http://openweathermap.org/img/wn/$imageName.png").apply(RequestOptions().circleCrop())
        .into(this)
}