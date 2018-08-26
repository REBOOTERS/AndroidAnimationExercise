package com.engineer.imitate.widget

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.engineer.imitate.R

/**
 *
 * @author: Rookie
 * @date: 2018-07-31 15:00
 * @version V1.0
 */

class CustomDialogFragment: DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_dialog,container,false)
        return view
    }
}