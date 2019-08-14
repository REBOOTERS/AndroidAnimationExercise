package com.engineer.imitate.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest

/**
 * author : engineer
 * e-mail : yingkongshi11@gmail.com
 * time   : 2018/7/31
 * desc   :
 * version: 1.0
 */

fun Context.isNetworkConnected():Boolean{
    val mConnectivityManager:ConnectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val mNetworkInfo = mConnectivityManager.activeNetworkInfo
    if (mNetworkInfo != null) {
        return mNetworkInfo.isAvailable
    }

    return false
}
