package home.smart.fly.animations.ui.activity

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.FlingAnimation
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import home.smart.fly.animations.R


class PhysicsViewActivity : AppCompatActivity() {

    private lateinit var image: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_physics_view)
        image = findViewById(R.id.image)
        findViewById<Button>(R.id.button2).text = "Fling"

        var velocity = 500f

        findViewById<Button>(R.id.button2).setOnClickListener {
            val flingAnimation = FlingAnimation(image, DynamicAnimation.X)
            flingAnimation.setStartVelocity(velocity)
            flingAnimation.friction = 0.5f
            flingAnimation.start()

            velocity = -velocity
        }

        findViewById<Button>(R.id.button3).setOnClickListener {
            val springAnimation = SpringAnimation(image, DynamicAnimation.Y)
            springAnimation.setStartValue(500f)

            val springForce = SpringForce()
            springForce.dampingRatio = SpringForce.DAMPING_RATIO_HIGH_BOUNCY
            springForce.stiffness = SpringForce.STIFFNESS_LOW
            springForce.finalPosition = image.y

            springAnimation.spring = springForce
            springAnimation.start()
        }
    }
}
