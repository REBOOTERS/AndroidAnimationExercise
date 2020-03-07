package com.engineer.android.game.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.webkit.JavascriptInterface
import com.snatik.matches.MemoryGameActivity

/**
 * Created on 2020/3/7.
 * @author rookie
 */
class GameRootActivity : BaseWebViewActivity() {
    override fun provideUrl(): String {
        return "file:///android_asset/game/index.html"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        webView.addJavascriptInterface(Hybrid(this),"hybrid")
    }

    override fun onResume() {
        super.onResume()
        webView.reload()
    }
}

class Hybrid(private var context: Context) {

    @JavascriptInterface
    fun go(path: String) {
        var intent : Intent? = null
        when (path) {
            "2048" -> intent = Intent(context,Game2048Activity::class.java)
            "schulte" -> intent = Intent(context,SchulteGridActivity::class.java)
            "towerBuild" -> intent = Intent(context,TowerBuilderActivity::class.java)
            "sensor" -> intent = Intent(context,SensorViewActivity::class.java)
            "memory" -> intent = Intent(context,MemoryGameActivity::class.java)
        }
        context.startActivity(intent)
    }
}