package com.example.weatherapp.viewmodels

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.example.weatherapp.HomeViewPagerFragmentDirections
import com.google.android.material.floatingactionbutton.FloatingActionButton

/**
 * Created by zsebe.akos on 2/18/2020.
 */
class HomeViewPagerViewModel internal constructor(
) : ViewModel() {
    fun onFabClicked(view: View) {
        view as FloatingActionButton
        val direction =
            HomeViewPagerFragmentDirections.actionViewPagerFragmentToLocationSearchFragment()
        view.findNavController().navigate(direction)
    }
}