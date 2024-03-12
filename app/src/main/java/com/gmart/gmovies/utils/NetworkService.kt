package com.gmart.gmovies.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

sealed interface ConnectionState {
    object Unknown : ConnectionState
    object Available : ConnectionState
    object Unavailable : ConnectionState
}

interface NetworkService {
    val connectionState: Flow<ConnectionState>
}

class NetworkServiceImpl @Inject constructor(context: Context) :
    NetworkService {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager


    override val connectionState: Flow<ConnectionState> = callbackFlow {
        val connectivityCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                trySend(ConnectionState.Available)
            }

            override fun onUnavailable() {
                trySend(ConnectionState.Unavailable)
            }

            override fun onLost(network: Network) {
                //trySend(ConnectionState.Unknown)
            }
        }

        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()

        connectivityManager.registerNetworkCallback(request, connectivityCallback)

        awaitClose {
            connectivityManager.unregisterNetworkCallback(connectivityCallback)
        }
    }
        .distinctUntilChanged()
        .flowOn(Dispatchers.IO)
}