package com.example.weatherapp

import android.os.Bundle
import android.view.View
import com.example.weatherapp.base.BaseFragment
import com.example.weatherapp.databinding.FragmentLocationSearchBinding
import com.example.weatherapp.utils.InjectorUtils
import com.example.weatherapp.viewmodels.LocationSearchViewModel

class LocationSearchFragment : BaseFragment<FragmentLocationSearchBinding, LocationSearchViewModel>(R.layout.fragment_location_search) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = InjectorUtils.provideLocationSearchViewModelFactory(this)
            .create(LocationSearchViewModel::class.java)

       // setToolbar(binding.toolbar,true)
    }
}