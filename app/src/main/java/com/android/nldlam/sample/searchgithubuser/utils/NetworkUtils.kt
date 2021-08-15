package com.android.nldlam.sample.searchgithubuser.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

object NetworkUtils {
    fun Context.hasNetworkAvailable(): Boolean {
        val connectivity = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivity.activeNetwork ?: return false
        val networkCapabilities = connectivity.getNetworkCapabilities(network)
        return networkCapabilities != null && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }
}