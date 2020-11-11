package home.smart.fly.animations.ui.activity

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import home.smart.fly.animations.R
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_orientation.*
import java.util.concurrent.TimeUnit

class OrientationActivity : AppCompatActivity() {
    private var disposable: Disposable? = null

    @SuppressLint("SetTextI18n")
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

        disposable = Observable.just(0).delay(1, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                val xy = IntArray(2)
                val start = System.nanoTime()
                pos_tv.getLocationOnScreen(xy)
                Log.e(
                    "zyq",
                    "cost time = ${System.nanoTime() - start}"
                )
                pos_tv.text = "position info: x=${xy[0]},y=${xy[1]}"

                val hour = TimeUnit.DAYS.toHours(1)

                Log.e("zyq", "hour is == $hour")
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }
}
