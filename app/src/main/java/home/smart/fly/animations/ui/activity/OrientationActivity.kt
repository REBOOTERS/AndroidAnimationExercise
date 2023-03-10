package home.smart.fly.animations.ui.activity

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.switchmaterial.SwitchMaterial
import home.smart.fly.animations.R
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

class OrientationActivity : AppCompatActivity() {
    private var disposable: Disposable? = null
    private lateinit var switcher: SwitchMaterial
    private lateinit var text: TextView
    private lateinit var pos_tv: TextView

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orientation)

        switcher = findViewById(R.id.switcher)
        text = findViewById(R.id.text)
        pos_tv = findViewById(R.id.pos_tv)


        Toast.makeText(this, "OrientationActivity-onCreate", Toast.LENGTH_SHORT).show()
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

        disposable = Observable.just(0).delay(1, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe {
            val xy = IntArray(2)
            val start = System.nanoTime()
            pos_tv.getLocationOnScreen(xy)
            Log.e(
                "zyq", "cost time = ${System.nanoTime() - start}"
            )
            pos_tv.text = "position info: x=${xy[0]},y=${xy[1]}"

            val hour = TimeUnit.DAYS.toHours(1)

            Log.e("zyq", "hour is == $hour")
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Log.e("newConfig", "onConfigurationChanged() called with: newConfig = $newConfig")
        Log.e("newConfig", "${newConfig.orientation}")
//        setContentView(R.layout.activity_orientation)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }
}
