package com.example.teqelmasr.helper

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET
import android.net.NetworkRequest
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.io.IOException
import java.net.InetSocketAddress
import javax.net.SocketFactory

class MyConnectivityManager {

    companion object{

        private val _state = MutableLiveData<Boolean>()
        val state : LiveData<Boolean> = _state
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .addCapability(NET_CAPABILITY_INTERNET)
            .build()
        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            // network is available for use
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                _state.postValue(execute(network.socketFactory))
            }

            // lost network connection
            override fun onLost(network: Network) {
                super.onLost(network)
                _state.postValue(false)
            }
        }

        fun execute(socketFactory: SocketFactory): Boolean {

            return try {
                val socket = socketFactory.createSocket() ?: throw IOException("Socket is null.")
                socket.connect(InetSocketAddress("8.8.8.8", 53), 1500)
                socket.close()
                true
            } catch (exception: IOException) {
                false
            }
        }
    }

}