package com.engineer.imitate.ui.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.WifiManager
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.engineer.imitate.R
import com.engineer.imitate.util.TimeUtilTool
import com.engineer.imitate.util.toastShort
import com.permissionx.guolindev.PermissionX
import java.util.concurrent.TimeUnit

/**
 * https://www.jianshu.com/p/f9a41bc1b046
 *
 * https://blog.csdn.net/Xia_Leon/article/details/82907420
 *
 * Wifi 扫描
 */
class WifiScanActivity : AppCompatActivity() {
    private var wifiManager: WifiManager? = null
    private var receiver: WifiBroadcastReceiver? = null
    private val TAG = "WifiScanActivity"

    private var currentWifiSsid = ""
    private var currentWifiBssid = ""
    private lateinit var resultTv: TextView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wifi_scan)

        initWifiManger()

        findViewById<Button>(R.id.manual_scan).setOnClickListener {
            wifiManager?.let {
                PermissionX.init(this)
                    .permissions(Manifest.permission.ACCESS_FINE_LOCATION)
                    .request { allGranted, _, _ ->
                        if (allGranted) {
                            progressBar.visibility = View.VISIBLE
                            val success = it.startScan()
                            Log.e(TAG, "onCreate: start scan $success")
                        }
                    }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initWifiManger() {
        wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val tv = findViewById<TextView>(R.id.wifi_ssid)
        resultTv = findViewById(R.id.wifi_scan_result)
        progressBar = findViewById(R.id.progress)

        wifiManager?.let {
            currentWifiSsid = it.connectionInfo.ssid
            currentWifiBssid = it.connectionInfo.bssid ?: ""


            tv.text = "当前连接 WIFI SSID: $currentWifiSsid BSSID: $currentWifiBssid"
        }


        receiver = WifiBroadcastReceiver()
        val intentFilter = IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
        registerReceiver(receiver, intentFilter)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }

    private inner class WifiBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.e(TAG, "onReceive: thread ${Thread.currentThread().name}")
            toastShort("scan success")

            progressBar.visibility = View.GONE
            intent?.let { it ->
                if (it.action == WifiManager.SCAN_RESULTS_AVAILABLE_ACTION) {
                    wifiManager?.scanResults?.let {
                        it.asSequence()
                            .sortedBy { sr -> sr.SSID }
                            .toMutableList()
                            .forEach { result ->
                                val actualTimeStamp = System.currentTimeMillis() - SystemClock
                                    .elapsedRealtime() + result.timestamp / 1000

                                val info = String.format(
                                    "%-35s,%-17s,%-20s,%-4.3s 小时,%-20d,%s",
                                    result.SSID,
                                    result.BSSID,
                                    result.timestamp,
                                    TimeUtilTool.millionSecondsToHour(result.timestamp / 1000),
                                    actualTimeStamp,
                                    TimeUtilTool.timeStampToDate(actualTimeStamp)
                                )

                                Log.e(TAG, info)

                                if (result.BSSID.equals(currentWifiBssid)) {
                                    val timestamp = result.timestamp
                                    val second = TimeUnit.MICROSECONDS.toSeconds(timestamp)
                                    val time = secondToTime(second)
                                    val sb = StringBuilder()
                                    sb.append(result.toString()).append("\n\n")
                                        .append("timestamp: $timestamp").append("\n")
                                        .append("second: $second").append("\n")
                                        .append("time: $time")
                                    resultTv.text = sb.toString()
                                }
                            }
                    }
                }
            }
        }
    }

    private fun secondToTime(secondP: Long): String {
        var second = secondP
        val days = second / 86400;//转换天数
        second %= 86400;//剩余秒数

        val hours = second / 3600;//转换小时数
        second %= 3600;//剩余秒数

        val minutes = second / 60;//转换分钟
        second %= 60;//剩余秒数

        if (0 < days) {
            return "$days 天:" + hours + "小时:" + minutes + "分:" + second + "秒"
        } else {
            return "$hours 小时:" + minutes + "分:" + second + "秒";
        }
    }
}