package com.engineer.android.game.ui

class SquareActivity : BaseWebViewActivity() {
    override fun provideUrl(): String {
        return "file:///android_asset/square/index.html"
    }
}
