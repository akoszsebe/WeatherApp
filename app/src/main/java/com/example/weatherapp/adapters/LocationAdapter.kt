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
import com.example.weatherapp.utils.UnitOfMeasurement

class LocationAdapter(private val unitOfMeasurement: UnitOfMeasurement) :
    ListAdapter<LocationWithWeather, RecyclerView.ViewHolder>(LocationDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return LocationViewHolder(
            ListItemWeatherLocationsBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ), unitOfMeasurement
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val plant = getItem(position)
        val hasDivider = itemCount != position + 1
        (holder as LocationViewHolder).bind(plant, hasDivider)
    }

    class LocationViewHolder(
        private val binding: ListItemWeatherLocationsBinding,
        unitOfMeasurement: UnitOfMeasurement
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.itemView.setOnClickListener {
                binding.location?.let { location ->
                    navigateToLocation(location, it)
                }
            }
            binding.unitOfMeasurement = unitOfMeasurement
        }

        private fun navigateToLocation(
            location: LocationWithWeather,
            view: View
        ) {
            val direction =
                HomeViewPagerFragmentDirections.actionViewPagerFragmentToLocationWeatherDetailFragment(
                    location.id
                )
            view.findNavController().navigate(direction)
        }

        fun bind(item: LocationWithWeather, hasDivider: Boolean) {
            binding.apply {
                location = item
                this.hasDivider = hasDivider
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