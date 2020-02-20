package com.example.weatherapp

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.example.weatherapp.base.BaseFragment
import com.example.weatherapp.databinding.FragmentWeatherDetailesBinding
import com.example.weatherapp.utils.InjectorUtils
import com.example.weatherapp.viewmodels.WeatherDetailsViewModel
import com.google.android.material.appbar.AppBarLayout

class WeatherDetailFragment : BaseFragment<FragmentWeatherDetailesBinding, WeatherDetailsViewModel>(R.layout.fragment_weather_detailes) {

    private val args: WeatherDetailFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = InjectorUtils.provideWeatherDetailsViewModelFactory(this,args.locationId)
            .create(WeatherDetailsViewModel::class.java)

        setToolbar(binding.toolbar,true)
        binding.appBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            val scale: Float = ((appBarLayout.totalScrollRange.toFloat() + verticalOffset.toFloat()/2) / appBarLayout.totalScrollRange.toFloat())
            scaleOption(binding.scaleView, scale)
        })
        binding.locationRefresh.setOnRefreshListener {
            subscribeUi(true)
        }
        subscribeUi(false)
    }

    private fun subscribeUi(online: Boolean) {
        binding.locationRefresh.isRefreshing = true
        disposables.add(viewModel.getFavoriteLocationsWeather(online)
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
    }

    private fun scaleOption(item: View?, scale: Float) {
        item?.animate()?.scaleX(scale)?.scaleY(scale)?.setDuration(0)?.start()
    }
}