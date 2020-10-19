package home.smart.fly.animations.ui.activity

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import home.smart.fly.animations.R
import kotlinx.android.synthetic.main.activity_orientation.*

class OrientationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orientation)
        switcher.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                switcher.text = "切竖屏"
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            } else {
                switcher.text = "切横屏"
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            }
        }
        text.movementMethod = ScrollingMovementMethod.getInstance()
    }
}
