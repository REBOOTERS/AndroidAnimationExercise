package com.engineer.imitate.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Route
import com.engineer.imitate.R
import com.engineer.imitate.util.hide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.reactivex.Observable

@Route(path = "/anim/motion_layout")
@SuppressLint("LogNotTimber")
class MotionLayoutFragment : Fragment() {
    val TAG = "MotionLayoutFragment"

    private lateinit var motion_layout: MotionLayout
    private lateinit var image_girl: ImageView
    private lateinit var add: FloatingActionButton
    private lateinit var image_btn: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_motion_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        motion_layout = view.findViewById(R.id.motion_layout)
        image_girl = view.findViewById(R.id.image_girl)
        add = view.findViewById(R.id.add)
        image_btn = view.findViewById(R.id.image_btn)

        motion_layout.setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {
                Log.d(
                    TAG, "onTransitionTrigger() called with: p1 = $p1, p2 = $p2, p3 = $p3"
                )
            }

            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
                Log.d(TAG, "onTransitionStarted() called with: p1 = $p1, p2 = $p2")
            }

            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {
                Log.d(
                    TAG, "onTransitionChange() called with: p1 = $p1, p2 = $p2, p3 = $p3"
                )
            }

            override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
                Log.d(TAG, "onTransitionCompleted() called with: p1 = $p1")
                image_girl.hide()
            }

        })
        image_girl.visibility = View.VISIBLE
        motion_layout.transitionToEnd()
        add.setOnClickListener {
            Toast.makeText(context, "1", Toast.LENGTH_SHORT).show()
        }

        image_btn.setOnLongClickListener {
            Log.e(TAG, "long click")
            true
        }

        Observable.just(1).map {
//                middle()
            }.onErrorReturn {
                it.printStackTrace()
            }.onExceptionResumeNext {
                Observable.just(20)
            }.subscribe({
                dealData(1)
            }, {
                it.printStackTrace()
            })
    }

    private fun dealData(i: Int) {
        Toast.makeText(context, i, Toast.LENGTH_SHORT).show()
    }

    private fun middle() {
        throw ExceptionInInitializerError("test")
    }
}