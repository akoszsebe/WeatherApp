package com.example.weatherapp

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil.setContentView
import com.example.weatherapp.databinding.ActivityWeatherBinding
import com.example.weatherapp.utils.ConnectionHelper
import com.example.weatherapp.utils.InjectorUtils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class WeatherActivity : AppCompatActivity() {

    private lateinit var connectionHelper: ConnectionHelper
    private lateinit var connectivityManager: ConnectivityManager
    private var textViewOffline: TextView? = null
    private var networkStateChangedDispose: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView<ActivityWeatherBinding>(this, R.layout.activity_weather)
        textViewOffline = findViewById(R.id.textview_offline)
        connectivityManager =
            this.baseContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectionHelper = InjectorUtils.provideConnectionHelper(this.applicationContext)
    }

    override fun onResume() {
        super.onResume()
        networkStateChangedDispose = naiveObserveNetworkStateChanged(
            connectivityManager,
            connectionHelper
        )?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe { networkState ->
                when (networkState) {
                    NetworkState.ONNLINE -> textViewOffline?.visibility = View.GONE
                    NetworkState.OFFLINE -> textViewOffline?.visibility = View.VISIBLE
                    else -> textViewOffline?.visibility = View.VISIBLE
                }
            }
    }

    override fun onPause() {
        super.onPause()
        networkStateChangedDispose?.dispose()
    }

    private fun naiveObserveNetworkStateChanged(
        connectivityManager: ConnectivityManager,
        connectionHelper: ConnectionHelper
    ): Observable<NetworkState>? {
        return Observable.create { emitter ->
            val networkCallback: NetworkCallback = object : NetworkCallback() {
                override fun onAvailable(network: Network) {
                    emitter.onNext(NetworkState.ONNLINE)
                }

                override fun onLost(network: Network) {
                    emitter.onNext(NetworkState.OFFLINE)
                }
            }
            emitter.setCancellable {
                connectivityManager.unregisterNetworkCallback(networkCallback)
            }
            when(connectionHelper.isOnline()){
                true -> emitter.onNext(NetworkState.ONNLINE)
                else -> emitter.onNext(NetworkState.OFFLINE)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                connectivityManager.registerDefaultNetworkCallback(networkCallback)
            } else {
                val request = NetworkRequest.Builder()
                    .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET).build()
                connectivityManager.registerNetworkCallback(request, networkCallback)
            }
        }
    }
}

enum class NetworkState {
    ONNLINE,
    OFFLINE
}
