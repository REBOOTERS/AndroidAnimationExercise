package com.engineer.android.game.ui

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.engineer.android.game.R
import kotlinx.android.synthetic.main.activity_shutlte.*

/**
 * https://github.com/jwenjian/schulte-grid
 */
class SchulteGridActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_shutlte)

        val settings = webView.settings
        settings.javaScriptEnabled = true
        settings.domStorageEnabled = true
        settings.databaseEnabled = true

        webView.loadUrl("file:///android_asset/schulte/index.html")
    }
}
