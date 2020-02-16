package com.example.weatherapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.weatherapp.adapters.LocationAdapter
import com.example.weatherapp.databinding.FragmentLocationsBinding
import com.example.weatherapp.utils.InjectorUtils
import com.example.weatherapp.viewmodels.LocationListViewModel

class LocationsFragment : Fragment() {

    private lateinit var binding: FragmentLocationsBinding

    private val viewModel: LocationListViewModel by viewModels {
        InjectorUtils.provideLocationsListViewModelFactory(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLocationsBinding.inflate(inflater, container, false)
        val adapter = LocationAdapter()
        binding.locationsList.adapter = adapter
        subscribeUi(adapter)
        return binding.root
    }

    private fun subscribeUi(adapter: LocationAdapter) {
        viewModel.getFavoriteLocationsWeather()
            ?.subscribe(
                { weather ->
                    adapter.submitList(weather)
                    binding.hasLocations = true
                },
                {
                    print(it)
                }
            )
    }

    override fun onDestroy() {
        super.onDestroy()

    }

}