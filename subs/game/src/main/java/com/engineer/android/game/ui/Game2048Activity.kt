package com.engineer.android.game.ui

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.Settings
import android.util.Log
import android.view.MotionEvent
import android.view.Window
import android.view.WindowManager
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.Toast
import com.engineer.android.game.R
import java.util.*

class Game2048Activity : AppCompatActivity() {

    private val MAIN_ACTIVITY_TAG = "2048_MainActivity"

    private lateinit var mWebView: WebView
    private var mLastBackPress: Long = 0
    private val mBackPressThreshold: Long = 3500
    private val IS_FULLSCREEN_PREF = "is_fullscreen_pref"
    private var mLastTouch: Long = 0
    private val mTouchThreshold: Long = 2000
    private var pressBackToast: Toast? = null

    @SuppressLint("SetJavaScriptEnabled", "ShowToast", "ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Don't show an action bar or title
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        // Enable hardware acceleration
        window.setFlags(
            WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
            WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED
        )

        // Apply previous setting about showing status bar or not
        applyFullScreen(isFullScreen())

        // Check if screen rotation is locked in settings
        var isOrientationEnabled = false
        try {
            isOrientationEnabled = Settings.System.getInt(
                contentResolver,
                Settings.System.ACCELEROMETER_ROTATION
            ) == 1
        } catch (e: Settings.SettingNotFoundException) {
            Log.d(MAIN_ACTIVITY_TAG, "Settings could not be loaded")
        }

        // If rotation isn't locked and it's a LARGE screen then add orientation changes based on sensor
        val screenLayout =
            resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK
        if ((screenLayout == Configuration.SCREENLAYOUT_SIZE_LARGE || screenLayout == Configuration.SCREENLAYOUT_SIZE_XLARGE) && isOrientationEnabled) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR
        }

        setContentView(R.layout.activity_game2048)

//        val changeLog = DialogChangeLog.newInstance(this)
//        if (changeLog.isFirstRun()) {
//            changeLog.getLogDialog().show()
//        }

        // Load webview with game
        mWebView = findViewById(R.id.mainWebView)
        val settings = mWebView.settings
        settings.javaScriptEnabled = true
        settings.domStorageEnabled = true
        settings.databaseEnabled = true
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH)
        settings.databasePath = filesDir.parentFile.path + "/databases"

        // If there is a previous instance restore it in the webview
        if (savedInstanceState != null) {
            mWebView.restoreState(savedInstanceState)
        } else {
            // Load webview with current Locale language
            mWebView.loadUrl("file:///android_asset/2048/index.html?lang=" + Locale.getDefault().language)
        }

//        Toast.makeText(application, R.string.toggle_fullscreen, Toast.LENGTH_SHORT).show()
        // Set fullscreen toggle on webview LongClick
        mWebView.setOnTouchListener { v, event ->
            // Implement a long touch action by comparing
            // time between action up and action down
            val currentTime = System.currentTimeMillis()
            if (event.action == MotionEvent.ACTION_UP && Math.abs(currentTime - mLastTouch) > mTouchThreshold) {
                val toggledFullScreen = !isFullScreen()
                saveFullScreen(toggledFullScreen)
                applyFullScreen(toggledFullScreen)
            } else if (event.action == MotionEvent.ACTION_DOWN) {
                mLastTouch = currentTime
            }
            // return so that the event isn't consumed but used
            // by the webview as well
            false
        }

        pressBackToast = Toast.makeText(
            applicationContext, R.string.press_back_again_to_exit,
            Toast.LENGTH_SHORT
        )
    }

    override fun onResume() {
        super.onResume()
        mWebView.loadUrl("file:///android_asset/2048/index.html?lang=" + Locale.getDefault().language)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mWebView.saveState(outState)
    }

    /**
     * Saves the full screen setting in the SharedPreferences
     *
     * @param isFullScreen boolean value
     */

    private fun saveFullScreen(isFullScreen: Boolean) {
        // save in preferences
        val editor = PreferenceManager.getDefaultSharedPreferences(this).edit()
        editor.putBoolean(IS_FULLSCREEN_PREF, isFullScreen)
        editor.apply()
    }

    private fun isFullScreen(): Boolean {
        return PreferenceManager.getDefaultSharedPreferences(this).getBoolean(
            IS_FULLSCREEN_PREF,
            true
        )
    }

    /**
     * Toggles the activity's fullscreen mode by setting the corresponding window flag
     *
     * @param isFullScreen boolean value
     */
    private fun applyFullScreen(isFullScreen: Boolean) {
        if (isFullScreen) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }

    /**
     * Prevents app from closing on pressing back button accidentally.
     * mBackPressThreshold specifies the maximum delay (ms) between two consecutive backpress to
     * quit the app.
     */

    override fun onBackPressed() {
        val currentTime = System.currentTimeMillis()
        if (Math.abs(currentTime - mLastBackPress) > mBackPressThreshold) {
            pressBackToast!!.show()
            mLastBackPress = currentTime
        } else {
            pressBackToast!!.cancel()
            super.onBackPressed()
        }
    }
}
