package com.example.weatherapp

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import com.example.weatherapp.base.BaseFragment
import com.example.weatherapp.databinding.FragmentLocationSearchBinding
import com.example.weatherapp.utils.InjectorUtils
import com.example.weatherapp.viewmodels.LocationSearchViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*


class LocationSearchFragment :
    BaseFragment<FragmentLocationSearchBinding, LocationSearchViewModel>(R.layout.fragment_location_search) {


    private lateinit var geocoder: Geocoder
    lateinit var adapter: ArrayAdapter<String>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = InjectorUtils.provideLocationSearchViewModelFactory(this)
            .create(LocationSearchViewModel::class.java)

        setToolbar(binding.toolbar, true)
        adapter = ArrayAdapter(
            this.requireContext(),
            R.layout.simple_spinner_item, viewModel.locationNameList
        )
        binding.listviewResults.adapter = adapter
        geocoder = Geocoder(this.requireContext(), Locale.ENGLISH)
        binding.searhEditext.addTextChangedListener {
            if (!it.isNullOrEmpty()) {
                disposables.add(viewModel.getFromLocationName(
                    geocoder,
                    it.toString()
                ).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe { locationList ->
                        viewModel.locationNameList.clear();
                        if (!locationList.isNullOrEmpty()) {
                            viewModel.locationList = locationList
                            for (i in locationList) {
                                if (i.featureName == null) {
                                    viewModel.locationNameList.add("unknown")
                                } else {
                                    viewModel.locationNameList.add(i.featureName)
                                }
                            }
                        }
                        adapter.notifyDataSetChanged()
                    })
            } else {
                viewModel.locationNameList.clear()
                adapter.notifyDataSetChanged()
            }
        }

    }
}