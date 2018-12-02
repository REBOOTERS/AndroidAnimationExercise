package com.engineer.imitate.fragments


import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.facade.annotation.Route
import com.andrefrsousa.superbottomsheet.SuperBottomSheetFragment

import com.engineer.imitate.R
import kotlinx.android.synthetic.main.fragment_bottom.*

/**
 * A simple [Fragment] subclass.
 *
 * https://github.com/andrefrsousa/SuperBottomSheet
 *
 */
@Route(path = "/anim/bottomsheet")
class BottomFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bottom, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        open_bottom_sheet.setOnClickListener {
            val sheet = MyBottomSheetFragment()
            sheet.show(childFragmentManager, BottomFragment::class.toString())
        }
    }

    class MyBottomSheetFragment : SuperBottomSheetFragment() {
        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            super.onCreateView(inflater, container, savedInstanceState)
            return inflater.inflate(R.layout.my_bottom_sheet_layout, container, false)
        }

        override fun getStatusBarColor() = Color.RED
    }


}
