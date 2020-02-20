package com.example.weatherapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.HomeViewPagerFragmentDirections
import com.example.weatherapp.data.model.LocationWithWeather
import com.example.weatherapp.databinding.ListItemWeatherLocationsBinding

class LocationAdapter :
    ListAdapter<LocationWithWeather, RecyclerView.ViewHolder>(LocationDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return LocationViewHolder(
            ListItemWeatherLocationsBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val plant = getItem(position)
        (holder as LocationViewHolder).bind(plant)
    }

    class LocationViewHolder(
        private val binding: ListItemWeatherLocationsBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.setClickListener {
                binding.location?.let { location ->
                    navigateToLocation(location, it)
                }
            }
        }

        private fun navigateToLocation(
            location: LocationWithWeather,
            view: View
        ) {
            val direction =
                HomeViewPagerFragmentDirections.actionViewPagerFragmentToWeatherDetailFragment(
                    location.id
                )
            view.findNavController().navigate(direction)
        }

        fun bind(item: LocationWithWeather) {
            binding.apply {
                location = item
                executePendingBindings()
            }
        }
    }
}

private class LocationDiffCallback : DiffUtil.ItemCallback<LocationWithWeather>() {

    override fun areItemsTheSame(
        oldItem: LocationWithWeather,
        newItem: LocationWithWeather
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: LocationWithWeather,
        newItem: LocationWithWeather
    ): Boolean {
        return oldItem == newItem
    }
}