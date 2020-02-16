package com.example.weatherapp

import android.Manifest
import android.annotation.SuppressLint
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.weatherapp.databinding.FragmentCurrentLocationBinding
import com.example.weatherapp.utils.InjectorUtils
import com.example.weatherapp.viewmodels.CurrentLocationViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class CurrentLocationFragment : Fragment() {

    private lateinit var binding: FragmentCurrentLocationBinding

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val viewModel: CurrentLocationViewModel by viewModels {
        InjectorUtils.provideCurrentLocationViewModelFactory(this)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCurrentLocationBinding.inflate(inflater, container, false)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this.context!!)
        subscribeUi()
        return binding.root
    }

    private fun subscribeUi() {
        viewModel.getCurrentLocationWeather()
            ?.subscribe(
                { weather ->
                    binding.location = weather
                    obtieneLocalizacion()
                },
                {
                    print(it)
                }
            )
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

    override fun onDestroy() {
        super.onDestroy()

    }
}