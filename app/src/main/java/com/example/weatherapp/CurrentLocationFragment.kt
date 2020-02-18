package com.example.weatherapp

import android.Manifest
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.weatherapp.base.BaseFragment
import com.example.weatherapp.databinding.FragmentCurrentLocationBinding
import com.example.weatherapp.utils.InjectorUtils
import com.example.weatherapp.viewmodels.CurrentLocationViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class CurrentLocationFragment : BaseFragment<FragmentCurrentLocationBinding,CurrentLocationViewModel>(R.layout.fragment_current_location) {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = InjectorUtils.provideCurrentLocationViewModelFactory(this).create(CurrentLocationViewModel::class.java)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this.context!!)
        subscribeUi()
    }

    private fun subscribeUi() {
        disposables.add(viewModel.getCurrentLocationWeather()
            .subscribe(
                { weather ->
                    binding.location = weather
                    obtieneLocalizacion()
                },
                {
                    print(it)
                }
            ))
    }

    private fun obtieneLocalizacion(){
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
//                latitude =  location?.latitude
//                longitude = location?.longitude
                var l = location?.latitude
                print(location?.toString())
            }.addOnFailureListener {
                print(it)
                if (Build.VERSION.SDK_INT >= 23)
                    this.requestPermissions( arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            }
    }
}