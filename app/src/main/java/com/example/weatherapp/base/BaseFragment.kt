package com.example.weatherapp.base

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.example.weatherapp.utils.LoaderDialog
import com.example.weatherapp.utils.SharedPrefs
import com.example.weatherapp.utils.UnitOfMeasurement
import io.reactivex.disposables.CompositeDisposable

abstract class BaseFragment<B : ViewDataBinding, VM : ViewModel>(private var layoutResId: Int) : Fragment() {

    var unitOfMeasurement: UnitOfMeasurement = UnitOfMeasurement.KELVIN
    lateinit var sharedPrefs: SharedPrefs
    protected lateinit var binding: B
    protected lateinit var viewModel: VM
    protected val disposables: CompositeDisposable = CompositeDisposable()
    private lateinit var progressBar : Dialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutResId, container, false)
        sharedPrefs = SharedPrefs(this.requireContext())
        unitOfMeasurement = sharedPrefs.getUnitOfMeasurement()
        progressBar = LoaderDialog.buildLoader(this.requireContext())
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!disposables.isDisposed) {
            disposables.dispose()
        }
    }

    fun showToastMessage(message: String?){
        Toast.makeText(this.requireContext(),message,Toast.LENGTH_SHORT).show()
    }

    fun showDialog(message: String?) {
        AlertDialog.Builder(this.requireContext())
            .setTitle("Info")
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    fun showErrorDialog(message: String?) {
        AlertDialog.Builder(this.requireContext())
            .setTitle("Error")
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    fun showDialogNoGps() {
        val builder = AlertDialog.Builder(this.requireContext())
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, id ->
                startActivityForResult(
                    Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    , 11)
            }
            .setNegativeButton("No") { dialog, id ->
                dialog.cancel()
            }
        val alert: AlertDialog  = builder.create()
        alert.show()
    }

    fun setToolbar(toolbar: Toolbar?, showHomeButton : Boolean) {
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(showHomeButton)
            it.setDisplayShowHomeEnabled(showHomeButton)
        }
    }

    fun showLoader(){
        progressBar.show()
    }

    fun hideLoader(){
        progressBar.dismiss()
    }
}