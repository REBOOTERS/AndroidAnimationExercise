package com.engineer.android.game.ui

import android.os.Bundle
import android.view.ViewGroup
import android.view.WindowManager
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity

/**
 * @author zhuyongging @ Zhihu Inc.
 * @since 03-03-2020
 */
abstract class BaseWebViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        val webView = WebView(this)
        addContentView(
            webView,
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )

        val settings = webView.settings
        settings.javaScriptEnabled = true
        settings.domStorageEnabled = true
        settings.databaseEnabled = true
        webView.loadUrl(provideUrl())
    }

    abstract fun provideUrl(): String
}