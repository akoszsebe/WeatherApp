package com.example.weatherapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.example.weatherapp.adapters.CURRENT_LOCATION_PAGE_INDEX
import com.example.weatherapp.adapters.LOCATIONS_PAGE_INDEX
import com.example.weatherapp.adapters.WeatherPagerAdapter
import com.example.weatherapp.databinding.FragmentViewPagerBinding

class HomeViewPagerFragment: Fragment(){

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentViewPagerBinding.inflate(inflater, container, false)
        val tabLayout = binding.tabs
        val viewPager = binding.viewPager

        viewPager.adapter = WeatherPagerAdapter(this)

        // Set the icon and text for each tab
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = getTabTitle(position)
        }.attach()

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)

        return binding.root
    }

    private fun getTabTitle(position: Int): String? {
        return when (position) {
            LOCATIONS_PAGE_INDEX -> getString(R.string.locations)
            CURRENT_LOCATION_PAGE_INDEX -> getString(R.string.current_location)
            else -> null
        }
    }
}