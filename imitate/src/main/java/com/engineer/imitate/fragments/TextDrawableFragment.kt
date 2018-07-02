package com.engineer.imitate.fragments


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.CircularProgressDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.engineer.imitate.R
import kotlinx.android.synthetic.main.fragment_text_drawable.*


/**
 * A simple [Fragment] subclass.
 *
 */
class TextDrawableFragment : Fragment() {

    private lateinit var mContext: Context

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        mContext = this!!.context!!
        return inflater.inflate(R.layout.fragment_text_drawable, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var progress = CircularProgressDrawable(mContext)
        progress.setStyle(CircularProgressDrawable.DEFAULT)
        textView.setCompoundDrawables(progress,null,null,null)
    }


}
