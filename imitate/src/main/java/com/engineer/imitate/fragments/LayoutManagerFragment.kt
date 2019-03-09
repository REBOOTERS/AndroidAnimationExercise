package com.engineer.imitate.fragments


import android.annotation.SuppressLint
import android.os.Bundle
import android.service.autofill.TextValueSanitizer
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.alibaba.android.arouter.facade.annotation.Route

import com.engineer.imitate.R
import kotlinx.android.synthetic.main.fragment_layout_manager.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
@Route(path = "/anim/layout_manager")
class LayoutManagerFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_layout_manager, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        for (i in 1..5) {
            val textview= TextView(context)
            textview.text = "this is $i"
            textview.setPadding(10,10,10,10)
            container.addView(textview)
        }


        for (i in 1..5) {
            val view = LayoutInflater.from(context).inflate(R.layout.image_item, null)
            val image = view.findViewById<ImageView>(R.id.image)
            image.setImageResource(R.drawable.ic_add_black_24dp)
            container.addView(view)
        }

        stack_view_layout_1.layoutDirection = LinearLayout.LAYOUT_DIRECTION_LTR
        stack_view_layout_2.layoutDirection = LinearLayout.LAYOUT_DIRECTION_RTL


    }


}
