package com.engineer.imitate.fragments


import android.animation.ValueAnimator
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.NestedScrollView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.facade.annotation.Route

import com.engineer.imitate.R
import kotlinx.android.synthetic.main.fragment_drag.*

/**
 * A simple [Fragment] subclass.
 *
 */
@Route(path = "/anim/drag")
class DragFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_drag, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nested_scrollview.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { p0, p1, p2, p3, p4 ->

            Log.e("TAG", "p0==$p0")
            Log.e("TAG", "p1==$p1")
            Log.e("TAG", "p2==$p2")
            Log.e("TAG", "p3==$p3")
            Log.e("TAG", "p4==$p4")
        })


        val anim = ValueAnimator.ofInt(0, 1000)
        anim.duration = 4000
        anim.addUpdateListener {
            Log.e("TAG", it.animatedValue.toString())
            Log.e("TAG", it.animatedFraction.toString())

            sliding_layout.smoothSlideTo(it.animatedFraction,0)

        }
        anim.start()
    }


}
