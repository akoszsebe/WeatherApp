package com.example.weatherapp

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.navigation.findNavController
import com.example.weatherapp.adapters.CURRENT_LOCATION_PAGE_INDEX
import com.example.weatherapp.adapters.LOCATIONS_PAGE_INDEX
import com.example.weatherapp.adapters.WeatherPagerAdapter
import com.example.weatherapp.base.BaseFragment
import com.example.weatherapp.databinding.FragmentViewPagerBinding
import com.example.weatherapp.utils.InjectorUtils
import com.example.weatherapp.viewmodels.HomeViewPagerViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayoutMediator

class HomeViewPagerFragment :
    BaseFragment<FragmentViewPagerBinding, HomeViewPagerViewModel>(R.layout.fragment_view_pager) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = InjectorUtils.provideHomeViewPagerViewModelFactory().create(
            HomeViewPagerViewModel::class.java
        )
        val tabLayout = binding.tabs
        val viewPager = binding.viewPager

        viewPager.adapter = WeatherPagerAdapter(this)

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = getTabTitle(position)
        }.attach()

        setToolbar(binding.toolbar, false)
        binding.callback = object : Callback {
            override fun onFabClicked(view: View?) {
                view as FloatingActionButton
                val direction =
                    HomeViewPagerFragmentDirections.actionViewPagerFragmentToLocationSearchFragment()
                view.findNavController().navigate(direction)
            }

            override fun onSettingsClicked(view: View?){
                view as ImageView
                val direction =
                    HomeViewPagerFragmentDirections.actionViewPagerFragmentToSettingsFragment()
                view.findNavController().navigate(direction)
            }
        }
    }

    private fun getTabTitle(position: Int): String? {
        return when (position) {
            LOCATIONS_PAGE_INDEX -> getString(R.string.locations)
            CURRENT_LOCATION_PAGE_INDEX -> getString(R.string.current_location)
            else -> null
        }
    }

    interface Callback {
        fun onFabClicked(view: View?)
        fun onSettingsClicked(view: View?)
    }
}