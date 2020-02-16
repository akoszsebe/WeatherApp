package com.example.weatherapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.HomeViewPagerFragmentDirections
import com.example.weatherapp.data.Location
import com.example.weatherapp.databinding.ListItemWeatherLocationsBinding

class LocationAdapter : ListAdapter<Location, RecyclerView.ViewHolder>(LocationDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return LocationViewHolder(ListItemWeatherLocationsBinding.inflate(
            LayoutInflater.from(parent.context), parent, false))
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
            location: Location,
            view: View
        ) {
            val direction =
                HomeViewPagerFragmentDirections.actionViewPagerFragmentToPlantDetailFragment(
                    location.locationId
                )
            view.findNavController().navigate(direction)
        }

        fun bind(item: Location) {
            binding.apply {
                location = item
                executePendingBindings()
            }
        }
    }
}

private class LocationDiffCallback : DiffUtil.ItemCallback<Location>() {

    override fun areItemsTheSame(oldItem: Location, newItem: Location): Boolean {
        return oldItem.locationId == newItem.locationId
    }

    override fun areContentsTheSame(oldItem: Location, newItem: Location): Boolean {
        return oldItem == newItem
    }
}