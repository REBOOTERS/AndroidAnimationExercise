package home.smart.fly.animations.webview

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ScrollView
import androidx.appcompat.app.AppCompatActivity
import home.smart.fly.animations.R

class NestedWebViewActivity : AppCompatActivity() {
    private val TAG = "NestedWebViewActivity"
    private val BASE_URL = "https://www.baidu.com"

    private lateinit var webView: CustomWebView
    private lateinit var scrollView: ScrollView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nested_web_view)
        webView = findViewById(R.id.webView)
        scrollView = findViewById(R.id.scrollView)

        webView.loadUrl(BASE_URL)
        val settings = webView.settings
        settings.javaScriptEnabled = true
        settings.useWideViewPort = true

        webView.webViewClient = (object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                return false
            }
        })

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            webView.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                Log.e(TAG, "webView---------scrollY=" + scrollY)
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            scrollView.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                Log.e(TAG, "ScrollView-scrollY=" + scrollY)
            }
        }

    }


}
