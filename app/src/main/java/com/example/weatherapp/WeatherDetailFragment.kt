package com.example.weatherapp

import android.os.Bundle
import android.text.format.DateUtils
import android.view.View
import androidx.navigation.fragment.navArgs
import com.example.weatherapp.adapters.Extentions.isTomorrow
import com.example.weatherapp.adapters.ForecastListAdapter
import com.example.weatherapp.base.BaseFragment
import com.example.weatherapp.databinding.FragmentWeatherDetailesBinding
import com.example.weatherapp.utils.InjectorUtils
import com.example.weatherapp.viewmodels.WeatherDetailsViewModel
import com.google.android.material.appbar.AppBarLayout

class WeatherDetailFragment :
    BaseFragment<FragmentWeatherDetailesBinding, WeatherDetailsViewModel>(R.layout.fragment_weather_detailes) {

    private val args: WeatherDetailFragmentArgs by navArgs()
    private lateinit var adapterToday: ForecastListAdapter
    private lateinit var adapterTomorrow: ForecastListAdapter
    private lateinit var adapterForTheRemaining3Days: ForecastListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapterToday = ForecastListAdapter()
        adapterTomorrow = ForecastListAdapter()
        adapterForTheRemaining3Days = ForecastListAdapter()
        viewModel = InjectorUtils.provideWeatherDetailsViewModelFactory(this, args.locationId)
            .create(WeatherDetailsViewModel::class.java)

        setToolbar(binding.toolbar, true)
        binding.appBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            val scale: Float =
                ((appBarLayout.totalScrollRange.toFloat() + verticalOffset.toFloat()) / appBarLayout.totalScrollRange.toFloat())
            scaleOption(binding.scaleView, scale)
            val fade: Float =
                1 - ((appBarLayout.totalScrollRange.toFloat() + verticalOffset.toFloat())/ appBarLayout.totalScrollRange.toFloat())
            fadeOption(binding.fadeView, fade)
        })
        fadeOption(binding.fadeView,1.0f)
        binding.forecastForToday.adapter = adapterToday
        binding.forecastForTomorrow.adapter = adapterTomorrow
        binding.forecastForTheRemaining3Days.adapter = adapterForTheRemaining3Days
        binding.locationRefresh.setOnRefreshListener {
            subscribeUi(true)
        }
        subscribeUi(false)
    }

    private fun subscribeUi(
        online: Boolean
    ) {
        binding.locationRefresh.isRefreshing = true
        disposables.add(viewModel.getWeatherForLocationWithId(online)
            .subscribe(
                { weather ->
                    binding.location = weather
                    binding.hasData = true
                    binding.locationRefresh.isRefreshing = false
                },
                {
                    binding.locationRefresh.isRefreshing = false
                    showErrorDialog(it.message!!)
                }
            )
        )
        disposables.add(viewModel.getFiveDayForecastForLocationWithId().subscribe(
            { fiveDayForecast ->
                adapterToday.submitList(fiveDayForecast.list
                    .filter { DateUtils.isToday(it.dt * 1000) })
                adapterTomorrow.submitList(fiveDayForecast.list
                    .filter { isTomorrow(it.dt * 1000) })
                adapterForTheRemaining3Days.submitList(fiveDayForecast.list
                    .filterNot { DateUtils.isToday(it.dt * 1000) }
                    .filterNot { isTomorrow(it.dt * 1000) })
            },
            {
                showErrorDialog(it.message!!)
            }
        ))
    }

    private fun scaleOption(item: View?, scale: Float) {
        item?.animate()?.scaleX(scale)?.scaleY(scale)?.setDuration(0)?.start()
    }

    private fun fadeOption(item: View?, fade: Float) {
        item?.animate()?.setDuration(0)?.alpha(fade)?.start()
    }
}