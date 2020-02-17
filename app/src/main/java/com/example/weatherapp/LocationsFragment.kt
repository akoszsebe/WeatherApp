package com.example.weatherapp

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.weatherapp.adapters.LocationAdapter
import com.example.weatherapp.base.BaseFragment
import com.example.weatherapp.databinding.FragmentLocationsBinding
import com.example.weatherapp.utils.InjectorUtils
import com.example.weatherapp.viewmodels.LocationListViewModel

class LocationsFragment : BaseFragment<FragmentLocationsBinding>(R.layout.fragment_locations) {

    private val viewModel: LocationListViewModel by viewModels {
        InjectorUtils.provideLocationsListViewModelFactory(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = LocationAdapter()
        binding.locationsList.adapter = adapter
        subscribeUi(adapter)
    }

    private fun subscribeUi(adapter: LocationAdapter) {
        disposables.add(viewModel.getFavoriteLocationsWeather()
            .subscribe(
                { weather ->
                    adapter.submitList(weather)
                    binding.hasLocations = true
                },
                {
                    print(it)
                }
            )
        )
    }

    override fun onDestroy() {
        super.onDestroy()

    }

}