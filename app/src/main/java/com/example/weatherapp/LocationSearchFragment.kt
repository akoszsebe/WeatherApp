package com.example.weatherapp

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.app.ActivityCompat
import androidx.navigation.findNavController
import com.example.weatherapp.base.BaseFragment
import com.example.weatherapp.databinding.FragmentLocationSearchBinding
import com.example.weatherapp.utils.InjectorUtils
import com.example.weatherapp.viewmodels.LocationSearchViewModel
import com.google.android.gms.location.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*


open class LocationSearchFragment :
    BaseFragment<FragmentLocationSearchBinding, LocationSearchViewModel>(R.layout.fragment_location_search) {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private val INTERVAL: Long = 2000
    private val FASTEST_INTERVAL: Long = 1000
    private val REQUEST_PERMISSION_LOCATION = 10
    private lateinit var geocoder: Geocoder
    lateinit var adapter: ArrayAdapter<String>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = InjectorUtils.provideLocationSearchViewModelFactory(this)
            .create(LocationSearchViewModel::class.java)

        if (!viewModel.connectionHelper.isOnline()){
            showErrorDialog("This page is working only in online mode")
        }

        setToolbar(binding.toolbar, true)
        adapter = ArrayAdapter(
            this.requireContext(),
            R.layout.simple_spinner_item, viewModel.locationNameList
        )
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this.requireContext())
        binding.listviewResults.adapter = adapter
        geocoder = Geocoder(this.requireContext(), Locale.ENGLISH)
        binding.searhEditext.addTextChangedListener(textWatcher)
        binding.listviewResults.setOnItemClickListener { _, _, position, _ ->
            setLocationName(viewModel.locationNameList[position])
            binding.hasLocationSuggestions = false
        }
        binding.buttonGps.setOnClickListener {
            if (checkPermissionForLocation(this.requireContext())) {
                val locationManager =
                    this.requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    showDialogNoGps()
                } else {
                    showLoader()
                    startLocationUpdates()
                }
            }
        }
        binding.coordinatorLayout.setOnClickListener {
            binding.hasLocationSuggestions = false
        }
        binding.showWeatherButton.setOnClickListener {
            disposables.add(
                viewModel.getLocationWeatherByName(binding.searhEditext.text.toString())
                    .subscribe(
                        { weather ->
                            binding.location = weather
                            binding.hasWeather = true
                        },
                        {
                            binding.hasWeather = false
                            showErrorDialog(it.message)
                        })
            )
        }
        binding.gotoDetailsButton.setOnClickListener {
            val direction =
                LocationSearchFragmentDirections.actionLocationSearchFragmentToLocationDetailFragment(
                    binding.location!!.id
                )
            view.findNavController().navigate(direction)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        stopLocationUpdates()
    }

    private fun startLocationUpdates() {
        locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = INTERVAL
        locationRequest.fastestInterval = FASTEST_INTERVAL

        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(locationRequest)
        val locationSettingsRequest = builder.build()

        val settingsClient = LocationServices.getSettingsClient(this.requireContext())
        settingsClient.checkLocationSettings(locationSettingsRequest)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this.requireContext())
        if (ActivityCompat.checkSelfPermission(
                this.requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this.requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.requestLocationUpdates(
            locationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }

    private fun setLocationName(text: String) {
        binding.searhEditext.removeTextChangedListener(textWatcher)
        binding.searhEditext.setText("")
        binding.searhEditext.append(text)
        binding.searhEditext.addTextChangedListener(textWatcher)
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(mLocationCallback)
    }

    fun checkPermissionForLocation(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                true
            } else {
                ActivityCompat.requestPermissions(
                    this.requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_PERMISSION_LOCATION
                )
                false
            }
        } else {
            true
        }
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            if (locationResult.lastLocation != null) {
                var result = geocoder.getFromLocation(
                    locationResult.lastLocation.latitude,
                    locationResult.lastLocation.longitude,
                    1
                )
                setLocationName(result[0].locality)
                hideLoader()
                stopLocationUpdates()
            }
        }
    }


    private var textWatcher = object : TextWatcher {
        override fun beforeTextChanged(
            charSequence: CharSequence, i: Int, i1: Int, i2: Int
        ) {
        }

        override fun onTextChanged(
            charSequence: CharSequence, i: Int, i1: Int, i2: Int
        ) {
        }

        override fun afterTextChanged(editable: Editable) {
            if (editable.toString().length > 3) {
                textChanged(editable)
            }
        }
    }

    private fun textChanged(it: Editable?) {
        if (!it.isNullOrEmpty()) {
            disposables.add(viewModel.getFromLocationName(
                geocoder,
                it.toString()
            ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe({ locationList ->
                    binding.hasLocationSuggestions = false
                    viewModel.locationNameList.clear()
                    if (!locationList.isNullOrEmpty()) {
                        viewModel.locationList = locationList
                        for (i in locationList) {
                            if (i.featureName == null) {
                                viewModel.locationNameList.add("unknown")
                            } else {
                                viewModel.locationNameList.add(i.featureName)
                                binding.hasLocationSuggestions = true
                            }
                        }
                    }
                    adapter.notifyDataSetChanged()
                }, {
                    showErrorDialog(it.message)
                })
            )
        } else {
            viewModel.locationNameList.clear()
            adapter.notifyDataSetChanged()
        }
    }
}