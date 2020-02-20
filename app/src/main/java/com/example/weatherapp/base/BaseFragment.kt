package com.example.weatherapp.base

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.example.weatherapp.utils.LoaderDialog
import io.reactivex.disposables.CompositeDisposable

abstract class BaseFragment<B : ViewDataBinding, VM : ViewModel>(private var layoutResId: Int) : Fragment() {

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
        progressBar = LoaderDialog.buildLoader(this.requireContext())
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!disposables.isDisposed) {
            disposables.dispose()
        }
    }

    fun showErrorDialog(message: String?) {
        AlertDialog.Builder(this.requireContext())
            .setTitle("Error")
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
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