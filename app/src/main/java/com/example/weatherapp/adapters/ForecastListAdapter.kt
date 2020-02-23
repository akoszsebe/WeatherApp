package com.example.weatherapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.data.model.WeatherListElement
import com.example.weatherapp.databinding.ListItemWeatherLocationsforcastBinding
import com.example.weatherapp.utils.UnitOfMeasurement

class ForecastListAdapter(private val unitOfMeasurement: UnitOfMeasurement) :
    ListAdapter<WeatherListElement, RecyclerView.ViewHolder>(ForecastDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ForecastViewHolder(
            ListItemWeatherLocationsforcastBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ), unitOfMeasurement
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        (holder as ForecastViewHolder).bind(item)
    }

    class ForecastViewHolder(
        private val binding: ListItemWeatherLocationsforcastBinding,
        unitOfMeasurement: UnitOfMeasurement
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.unitOfMeasurement = unitOfMeasurement
        }
        fun bind(item: WeatherListElement) {
            binding.apply {
                location = item
                executePendingBindings()
            }
        }
    }
}

private class ForecastDiffCallback : DiffUtil.ItemCallback<WeatherListElement>() {

    override fun areItemsTheSame(
        oldItem: WeatherListElement,
        newItem: WeatherListElement
    ): Boolean {
        return oldItem.dt == newItem.dt
    }

    override fun areContentsTheSame(
        oldItem: WeatherListElement,
        newItem: WeatherListElement
    ): Boolean {
        return oldItem == newItem
    }
}