package home.smart.fly.animations.ui.activity

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import home.smart.fly.animations.R
import kotlinx.android.synthetic.main.activity_lottie_animation_view.*
import kotlinx.android.synthetic.main.content_lottie_animation_view.*

class LottieAnimationViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lottie_animation_view)
        setSupportActionBar(toolbar)



        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        start.setOnClickListener {
            animation_view.cancelAnimation()
            animation_view.setAnimation("PinJump.json")
            animation_view.playAnimation()
        }

        success.setOnClickListener {
            animation_view.cancelAnimation()
            animation_view.setAnimation("ProgressSuccess.json")
            animation_view.loop(false)
            animation_view.playAnimation()
        }

        error.setOnClickListener {
            animation_view.cancelAnimation()
            animation_view.setAnimation("x_pop.json")
            animation_view.playAnimation()
        }
    }

}








