package home.smart.fly.animations.ui.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.snackbar.Snackbar
import home.smart.fly.animations.R

class LottieAnimationViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lottie_animation_view)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)



        findViewById<View>(R.id.fab).setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show()
        }
        val animation_view: LottieAnimationView = findViewById(R.id.animation_view)
        findViewById<View>(R.id.start).setOnClickListener {
            animation_view.cancelAnimation()
            animation_view.setAnimation("PinJump.json")
            animation_view.playAnimation()
        }

        findViewById<View>(R.id.success).setOnClickListener {
            animation_view.cancelAnimation()
            animation_view.setAnimation("ProgressSuccess.json")
            animation_view.loop(false)
            animation_view.playAnimation()
        }

        findViewById<View>(R.id.error).setOnClickListener {
            animation_view.cancelAnimation()
            animation_view.setAnimation("x_pop.json")
            animation_view.playAnimation()
        }
    }

}








