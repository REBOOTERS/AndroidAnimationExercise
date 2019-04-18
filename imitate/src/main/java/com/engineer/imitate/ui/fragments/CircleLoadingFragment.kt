package com.engineer.imitate.ui.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.facade.annotation.Route

import com.engineer.imitate.R



/**
 * A simple [Fragment] subclass.
 *
 */

@Route(path = "/anim/circleLoading")
class CircleLoadingFragment : androidx.fragment.app.Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_circle_loading, container, false)
    }


}
