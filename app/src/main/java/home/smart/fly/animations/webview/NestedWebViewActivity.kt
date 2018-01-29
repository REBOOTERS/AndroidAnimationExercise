package home.smart.fly.animations.webview

import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import home.smart.fly.animations.R
import kotlinx.android.synthetic.main.activity_nested_web_view.*

class NestedWebViewActivity : AppCompatActivity() {
    private val TAG = "NestedWebViewActivity"
    private val BASE_URL = "https://www.baidu.com"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nested_web_view)
        webView.loadUrl(BASE_URL)
        var settings = webView.settings
        settings.javaScriptEnabled = true
        settings.useWideViewPort = true

        webView.webViewClient = (object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                return false
            }
        })

        webView.setOnTouchListener { v, event ->

        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            webView.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                Log.e(TAG, "webView---------scrollY=" + scrollY)
            }
        }



        scrollView.isEnabled = false

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            scrollView.setOnScrollChangeListener(object : View.OnScrollChangeListener {
                override fun onScrollChange(v: View?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int) {
                    Log.e(TAG, "ScrollView-scrollY=" + scrollY)
                }

            })
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            scrollView.addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
                override fun onLayoutChange(v: View?, left: Int, top: Int, right: Int, bottom: Int, oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int) {
                    Log.e(TAG, "ScrollView-top=" + top)

                }

            })
        }

    }


}
