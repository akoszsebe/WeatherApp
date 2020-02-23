package com.example.weatherapp

import android.content.Context
import android.location.LocationManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ArrayAdapter
import androidx.navigation.findNavController
import com.example.weatherapp.base.BaseFragment
import com.example.weatherapp.databinding.FragmentLocationSearchBinding
import com.example.weatherapp.utils.InjectorUtils
import com.example.weatherapp.viewmodels.LocationSearchViewModel
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult

class LocationSearchFragment :
    BaseFragment<FragmentLocationSearchBinding, LocationSearchViewModel>(R.layout.fragment_location_search) {

    private lateinit var adapter: ArrayAdapter<String>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = InjectorUtils.provideLocationSearchViewModelFactory(this)
            .create(LocationSearchViewModel::class.java)

        if (!viewModel.connectionHelper.isOnline()) {
            showErrorDialog("This page is working only in online mode")
        }
        binding.unitOfMeasurement = unitOfMeasurement
        setToolbar(binding.toolbar, true)
        adapter = ArrayAdapter(
            this.requireContext(),
            R.layout.simple_spinner_item, viewModel.locationNameList
        )
        binding.listviewResults.adapter = adapter
        binding.searhEditext.addTextChangedListener(textWatcher)
        binding.listviewResults.setOnItemClickListener { _, _, position, _ ->
            setLocationName(viewModel.locationNameList[position])
            binding.hasLocationSuggestions = false
        }
        binding.buttonGps.setOnClickListener {
            if (viewModel.locationHelper.checkPermissionForLocation(this.requireActivity())) {
                val locationManager =
                    this.requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    showDialogNoGps()
                } else {
                    showLoader()
                    viewModel.locationHelper.startLocationUpdates(
                        this.requireContext(),
                        locationCallback
                    )
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
        binding.saveFavoriteButton.setOnClickListener {
            disposables.add(viewModel.addToFavorites(binding.location!!.id).subscribe({
                showToastMessage(getString(R.string.added_to_favorites))
            }, {
                showErrorDialog(it.message)
            }))
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.locationHelper.stopLocationUpdates(locationCallback)
    }

    private fun setLocationName(text: String) {
        binding.searhEditext.removeTextChangedListener(textWatcher)
        binding.searhEditext.setText("")
        binding.searhEditext.append(text)
        binding.searhEditext.addTextChangedListener(textWatcher)
    }


    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            if (locationResult.lastLocation != null) {
                val result = viewModel.geocoder.getFromLocation(
                    locationResult.lastLocation.latitude,
                    locationResult.lastLocation.longitude,
                    1
                )
                setLocationName(result[0].locality)
                hideLoader()
                viewModel.locationHelper.stopLocationUpdates(this)
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
            disposables.add(
                viewModel.getFromLocationName(
                    it.toString()
                ).subscribe({ locationList ->
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