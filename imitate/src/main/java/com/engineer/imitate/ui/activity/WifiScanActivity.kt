package com.engineer.imitate.ui.activity

import android.net.wifi.WifiManager
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.engineer.imitate.R

class WifiScanActivity : AppCompatActivity() {
    private var wifiManager: WifiManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wifi_scan)

        initWifiManger()

        findViewById<Button>(R.id.manual_scan).setOnClickListener {

        }
    }

    private fun initWifiManger() {

    }
}