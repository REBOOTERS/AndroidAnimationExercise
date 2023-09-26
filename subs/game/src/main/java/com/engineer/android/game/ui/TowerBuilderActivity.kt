package com.engineer.android.game.ui


class TowerBuilderActivity : BaseWebViewActivity() {

    override fun provideUrl(): String {
        return "file:///android_asset/tower/index.html"
    }
}
