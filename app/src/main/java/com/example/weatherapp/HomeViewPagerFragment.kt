package com.example.weatherapp

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherapp.adapters.CURRENT_LOCATION_PAGE_INDEX
import com.example.weatherapp.adapters.LOCATIONS_PAGE_INDEX
import com.example.weatherapp.adapters.WeatherPagerAdapter
import com.example.weatherapp.base.BaseFragment
import com.example.weatherapp.databinding.FragmentViewPagerBinding
import com.google.android.material.tabs.TabLayoutMediator

class HomeViewPagerFragment : BaseFragment<FragmentViewPagerBinding>(R.layout.fragment_view_pager) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tabLayout = binding.tabs
        val viewPager = binding.viewPager

        viewPager.adapter = WeatherPagerAdapter(this)

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = getTabTitle(position)
        }.attach()

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
    }

    private fun getTabTitle(position: Int): String? {
        return when (position) {
            LOCATIONS_PAGE_INDEX -> getString(R.string.locations)
            CURRENT_LOCATION_PAGE_INDEX -> getString(R.string.current_location)
            else -> null
        }
    }
}