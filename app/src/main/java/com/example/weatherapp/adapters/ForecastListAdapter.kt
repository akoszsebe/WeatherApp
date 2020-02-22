package com.example.weatherapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.data.model.ListElement
import com.example.weatherapp.databinding.ListItemWeatherLocationsforcastBinding

class ForecastListAdapter : ListAdapter<ListElement, RecyclerView.ViewHolder>(ForecastDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ForcastViewHolder(
            ListItemWeatherLocationsforcastBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        (holder as ForcastViewHolder).bind(item)
    }

    class ForcastViewHolder(
        private val binding: ListItemWeatherLocationsforcastBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ListElement) {
            binding.apply {
                location = item
                executePendingBindings()
            }
        }
    }
}

private class ForecastDiffCallback : DiffUtil.ItemCallback<ListElement>() {

    override fun areItemsTheSame(
        oldItem: ListElement,
        newItem: ListElement
    ): Boolean {
        return oldItem.dt == newItem.dt
    }

    override fun areContentsTheSame(
        oldItem: ListElement,
        newItem: ListElement
    ): Boolean {
        return oldItem == newItem
    }
}