package com.example.weatherapp

import android.os.Bundle
import android.view.View
import com.example.weatherapp.adapters.LocationAdapter
import com.example.weatherapp.base.BaseFragment
import com.example.weatherapp.databinding.FragmentLocationsBinding
import com.example.weatherapp.utils.InjectorUtils
import com.example.weatherapp.viewmodels.LocationListViewModel

class LocationsFragment : BaseFragment<FragmentLocationsBinding,LocationListViewModel>(R.layout.fragment_locations) {

    private lateinit var adapter: LocationAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = LocationAdapter()
        viewModel = InjectorUtils.provideLocationsListViewModelFactory(this).create(LocationListViewModel::class.java)
        binding.locationsList.adapter = adapter
        binding.locationsListRefresh.setOnRefreshListener {
            subscribeUi(adapter)
        }
        subscribeUi(adapter)
    }

    private fun subscribeUi(adapter: LocationAdapter) {
        disposables.add(viewModel.getFavoriteLocationsWeather()
            .subscribe(
                { weather ->
                    adapter.submitList(weather)
                    binding.hasLocations = true
                    binding.locationsListRefresh.isRefreshing = false
                },
                {
                    binding.locationsListRefresh.isRefreshing = false
                }
            )
        )
    }

}