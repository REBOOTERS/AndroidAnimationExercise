package com.engineer.imitate.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.MotionLayout.TransitionListener
import com.engineer.imitate.KotlinRootActivity
import com.engineer.imitate.R
import com.engineer.imitate.util.SystemUtil
import com.gyf.immersionbar.ImmersionBar
import kotlinx.android.synthetic.main.activity_fantasy_splash.*

class FantasySplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ImmersionBar.with(this)
            .fitsSystemWindows(true)
            .statusBarColor(R.color.colorPrimary).init()
        if (SystemUtil.getDeviceBrand() == "Xiaomi") {
            startActivity(Intent(this@FantasySplashActivity, KotlinRootActivity::class.java))
        }
        setContentView(R.layout.activity_fantasy_splash)

        motion_layout.setTransitionListener(object : TransitionListener {
            override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
                startActivity(Intent(this@FantasySplashActivity, KotlinRootActivity::class.java))
            }

            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {}

            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {}

            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {}
        })

    }
}