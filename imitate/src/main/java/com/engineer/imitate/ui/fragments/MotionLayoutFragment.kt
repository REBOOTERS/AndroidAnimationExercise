package com.engineer.imitate.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Route
import com.engineer.imitate.R
import kotlinx.android.synthetic.main.fragment_motion_layout.*

@Route(path = "/anim/motion_layout")
class MotionLayoutFragment : Fragment() {
    val TAG = "MotionLayoutFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_motion_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        motion_layout.setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {
                Log.d(
                    TAG,
                    "onTransitionTrigger() called with: p1 = $p1, p2 = $p2, p3 = $p3"
                )
            }

            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
                Log.d(TAG, "onTransitionStarted() called with: p1 = $p1, p2 = $p2")
            }

            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {
                Log.d(
                    TAG,
                    "onTransitionChange() called with: p1 = $p1, p2 = $p2, p3 = $p3"
                )
            }

            override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
                Log.d(TAG, "onTransitionCompleted() called with: p1 = $p1")
            }

        })
        motion_layout.startLayoutAnimation()
    }
}