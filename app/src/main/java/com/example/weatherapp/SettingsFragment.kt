package com.example.weatherapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.appcompat.widget.AppCompatSpinner
import com.example.weatherapp.base.BaseFragment
import com.example.weatherapp.databinding.FragmentSettingsBinding
import com.example.weatherapp.service.WeatherSyncJobService
import com.example.weatherapp.utils.AutoRefresh
import com.example.weatherapp.utils.InjectorUtils
import com.example.weatherapp.utils.UnitOfMeasurement
import com.example.weatherapp.viewmodels.SettingsViewModel


class SettingsFragment : BaseFragment<FragmentSettingsBinding, SettingsViewModel>(R.layout.fragment_settings) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = InjectorUtils.provideSettingsViewModelFactory().create(SettingsViewModel::class.java)
        setToolbar(binding.toolbar,true)
        setupSpinner(R.array.units_array,binding.spinnerUnit)
        setupSpinner(R.array.auto_refresh_array,binding.spinnerAutoRefresh)
        binding.spinnerUnit.setSelection(unitOfMeasurement.value)
        binding.spinnerUnit.onItemSelectedListener = object : OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                unitOfMeasurement = UnitOfMeasurement.valueOf(position)
                this@SettingsFragment.sharedPrefs.setUnitOfMeasurement(unitOfMeasurement)
            }

        }
        binding.spinnerAutoRefresh.setSelection(sharedPrefs.getAutoRefresh().ordinal)
        binding.spinnerAutoRefresh.onItemSelectedListener = object : OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                val autoRefresh = AutoRefresh.valueOf(position)
                this@SettingsFragment.sharedPrefs.setAutoRefresh(autoRefresh)
                when(autoRefresh){
                    AutoRefresh.NEVER -> WeatherSyncJobService.cancel(this@SettingsFragment.requireContext())
                    else -> WeatherSyncJobService.schedule(this@SettingsFragment.requireContext(),autoRefresh.value)
                }
            }
        }
        binding.textViewDeveloper.setOnClickListener {
            val viewIntent = Intent(
                "android.intent.action.VIEW",
                Uri.parse("https://www.linkedin.com/in/zsebe-akos-b581b9139")
            )
            startActivity(viewIntent)
        }
        binding.textViewCode.setOnClickListener {
            val viewIntent = Intent(
                "android.intent.action.VIEW",
                Uri.parse("https://github.com/akoszsebe/WeatherApp")
            )
            startActivity(viewIntent)
        }
    }

    private fun setupSpinner(arrayId : Int, spinner: AppCompatSpinner) {
        val adapter: ArrayAdapter<*> = ArrayAdapter.createFromResource(
            this.requireContext(),
            arrayId, R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        spinner.adapter = adapter
    }
}