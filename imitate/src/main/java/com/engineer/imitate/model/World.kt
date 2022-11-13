package com.engineer.imitate.model

import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.zdog.library.render.*

//open class World : ZdogDrawable() {
//    init {
//        illo.alpha(0f)
//    }
//
//    fun rotate(rotate: Boolean) {
//        if (rotate) {
//            illo.animate {
//                interpolator = FastOutSlowInInterpolator()
//                update {
//                    val theta = it * TAU
//                    val delta = TAU * -3 / 64
//                    illo.rotate.y = (Math.sin(theta) * delta).toFloat()
//                    illo.rotate.x = ((Math.cos(theta) * -0.5 + 0.5) * delta).toFloat()
//                }
//            }.duration(1500).repeat().start()
//        } else {
//            illo.getAnimator(Anchor.AnimatorType.Custom)?.repeatCount = 0
//        }
//    }
//}