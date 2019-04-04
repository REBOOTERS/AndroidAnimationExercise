package home.smart.fly.animations.ui.activity

import android.os.Bundle
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.FlingAnimation
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import androidx.appcompat.app.AppCompatActivity
import home.smart.fly.animations.R
import kotlinx.android.synthetic.main.activity_physics_view.*


class PhysicsViewActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_physics_view)
        button2.text="Fling"

        var velocity=500f

        button2.setOnClickListener{
            val flingAnimation=FlingAnimation(image, DynamicAnimation.X)
            flingAnimation.setStartVelocity(velocity)
            flingAnimation.friction = 0.5f
            flingAnimation.start()

            velocity=-velocity
        }

        button3.setOnClickListener {
            val springAnimation=SpringAnimation(image,DynamicAnimation.Y)
            springAnimation.setStartValue(500f)

            val springForce=SpringForce()
            springForce.dampingRatio=SpringForce.DAMPING_RATIO_HIGH_BOUNCY
            springForce.stiffness=SpringForce.STIFFNESS_LOW
            springForce.finalPosition = image.y

            springAnimation.spring = springForce
            springAnimation.start()
        }
    }
}
