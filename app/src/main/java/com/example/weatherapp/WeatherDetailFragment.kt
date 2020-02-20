package com.example.weatherapp

import android.os.Bundle
import android.view.View
import com.example.weatherapp.base.BaseFragment
import com.example.weatherapp.databinding.FragmentWeatherDetailesBinding
import com.example.weatherapp.utils.InjectorUtils
import com.example.weatherapp.viewmodels.WeatherDetailsViewModel

class WeatherDetailFragment : BaseFragment<FragmentWeatherDetailesBinding, WeatherDetailsViewModel>(R.layout.fragment_weather_detailes) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = InjectorUtils.provideWeatherDetailsViewModelFactory(this)
            .create(WeatherDetailsViewModel::class.java)
    }
}