package com.engineer.imitate.ui.fragments


import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.alibaba.android.arouter.facade.annotation.Route

import com.engineer.imitate.R


/**
 * A simple [Fragment] subclass.
 *
 */
@Route(path = "/anim/constraint")
class ConstraintSetAnimFragment : androidx.fragment.app.Fragment() {

    private var isNormal = false
    private lateinit var mBaseLayout: ConstraintLayout

    private var mNorSet = ConstraintSet()
    private var mBigSet = ConstraintSet()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_constraint_set_anim_normal, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBaseLayout = view.findViewById(R.id.constraintset)

        mNorSet.clone(mBaseLayout)
        mBigSet.load(context, R.layout.fragment_constraint_set_anim_large)
        view.findViewById<ImageView>(R.id.imageView).setOnClickListener {
            TransitionManager.beginDelayedTransition(mBaseLayout)
            if (isNormal) {
                mNorSet.applyTo(mBaseLayout)
            } else {
                mBigSet.applyTo(mBaseLayout)
            }
            isNormal = !isNormal
        }
    }


}
