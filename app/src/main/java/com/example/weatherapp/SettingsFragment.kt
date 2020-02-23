package com.example.weatherapp

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.widget.AppCompatSpinner
import com.example.weatherapp.base.BaseFragment
import com.example.weatherapp.databinding.FragmentSettingsBinding
import com.example.weatherapp.utils.Extensions.of
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
        binding.spinnerUnit.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                unitOfMeasurement = UnitOfMeasurement.valueOf(position)
                this@SettingsFragment.sharedPrefs.setUnitOfMeasurement(unitOfMeasurement)
            }

        }

        3.0.of(UnitOfMeasurement.CELSIUS)
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