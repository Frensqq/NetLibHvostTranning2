package com.example.netlib.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.provider.CloudMediaProviderContract
import com.example.netlib.domain.network.Network

/**
 * NetworkImpl - проверка подключения к сети
 *
 * @param context - android context
 *
 */
internal class NetworkImpl(private val context: Context): Network {
    override fun isOnline(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network)?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)

    }
}