package com.example.weatherapp

import android.content.Context
import android.location.LocationManager
import android.os.Bundle
import android.text.format.DateUtils
import android.view.View
import androidx.core.widget.NestedScrollView
import com.example.weatherapp.adapters.ForecastListAdapter
import com.example.weatherapp.base.BaseFragment
import com.example.weatherapp.databinding.FragmentCurrentLocationBinding
import com.example.weatherapp.utils.AnimationUtils
import com.example.weatherapp.utils.Extensions
import com.example.weatherapp.utils.InjectorUtils
import com.example.weatherapp.utils.LocationHelper
import com.example.weatherapp.viewmodels.CurrentLocationViewModel
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult


class CurrentLocationFragment :
    BaseFragment<FragmentCurrentLocationBinding, CurrentLocationViewModel>(R.layout.fragment_current_location) {

    private lateinit var locationHelper: LocationHelper
    private lateinit var adapterToday: ForecastListAdapter
    private lateinit var adapterTomorrow: ForecastListAdapter
    private lateinit var adapterForTheRemaining3Days: ForecastListAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapterToday = ForecastListAdapter(unitOfMeasurement)
        adapterTomorrow = ForecastListAdapter(unitOfMeasurement)
        adapterForTheRemaining3Days = ForecastListAdapter(unitOfMeasurement)
        locationHelper = LocationHelper()
        viewModel = InjectorUtils.provideCurrentLocationViewModelFactory(this)
            .create(CurrentLocationViewModel::class.java)
        binding.unitOfMeasurement = unitOfMeasurement
        setToolbar(binding.toolbar, false)
        binding.scrollView.setOnScrollChangeListener { _: NestedScrollView?, _: Int, scrollY: Int, _: Int, _: Int ->
            val scale: Float =
                1f - scrollY / (this.view?.height!! * 0.15f)
            if (scale > 0) {
                AnimationUtils.scaleOption(binding.scaleView, scale)
            }
            val fade: Float =
                (scrollY / (this.view?.height!! * 0.3f))
            AnimationUtils.fadeOption(binding.fadeView, fade)
            binding.showToolbarButtons = showFavorite(fade)
        }
        AnimationUtils.fadeOption(binding.fadeView, 0.0f)
        binding.forecastForToday.adapter = adapterToday
        binding.forecastForTomorrow.adapter = adapterTomorrow
        binding.forecastForTheRemaining3Days.adapter = adapterForTheRemaining3Days
        binding.locationRefresh.setOnRefreshListener {
            checkCurrentLocation(true)
        }
        binding.buttonFavorite.setOnClickListener {
            if (binding.isFavorite) {
                disposables.add(
                    viewModel.removeFromFavorites().subscribe(
                        {
                            binding.isFavorite = false
                        },
                        {
                            showErrorDialog(it.message)
                        })
                )
            } else {
                disposables.add(
                    viewModel.addToFavorites().subscribe(
                        {
                            binding.isFavorite = true
                        },
                        {
                            showErrorDialog(it.message)
                        })
                )
            }
        }
        binding.buttonGps.setOnClickListener {
            if (locationHelper.checkPermissionForLocation(this.requireActivity())) {
                val locationManager =
                    this.requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    showDialogNoGps()
                } else {
                    showLoader()
                    locationHelper.startLocationUpdates(this.requireContext(), locationCallback)
                }
            }
        }
        checkCurrentLocation(false)
    }

    private fun checkCurrentLocation(online: Boolean) {
        viewModel.locationId = sharedPrefs.getCurrentLocation()
        if (viewModel.locationId != 0L) {
            subscribeUi(online)
        } else {

        }
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
                    .filter { Extensions.isTomorrow(it.dt * 1000) })
                adapterForTheRemaining3Days.submitList(fiveDayForecast.list
                    .filterNot { DateUtils.isToday(it.dt * 1000) }
                    .filterNot { Extensions.isTomorrow(it.dt * 1000) })
            },
            {
                showErrorDialog(it.message!!)
            }
        ))
        disposables.add(
            viewModel.isFavoriteLocation().subscribe(
                { isFavorite ->
                    binding.isFavorite = isFavorite
                },
                {
                    showErrorDialog(it.message!!)
                })
        )
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            if (locationResult.lastLocation != null) {
                locationHelper.stopLocationUpdates(this)
                disposables.add(
                    viewModel.getCurrentLocationWeather(
                        locationResult.lastLocation.latitude,
                        locationResult.lastLocation.longitude
                    ).subscribe(
                        {
                            sharedPrefs.setCurrentLocation(it.id)
                            hideLoader()
                            subscribeUi(false)
                        },
                        {
                            hideLoader()
                            showErrorDialog(it.message)
                        })
                )

            }
        }
    }

    private fun showFavorite(fade: Float): Boolean {
        return !fade.equals(0.0f)
    }

}