package com.engineer.imitate.fragments


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.facade.annotation.Route
import com.engineer.imitate.R


/**
 * A simple [Fragment] subclass.
 *
 */
@Route(path = "/anim/textDrawable")
class TextDrawableFragment : Fragment() {

    private lateinit var mContext: Context

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        mContext = this!!.context!!
        return inflater.inflate(R.layout.fragment_text_drawable, container, false)
    }



}
