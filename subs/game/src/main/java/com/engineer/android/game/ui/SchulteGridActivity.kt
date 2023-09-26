package com.engineer.android.game.ui

/**
 * https://github.com/jwenjian/schulte-grid
 */
class SchulteGridActivity : BaseWebViewActivity() {

    override fun provideUrl(): String {
        return "file:///android_asset/schulte/index.html"
    }
}
