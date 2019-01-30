package com.engineer.imitate.fragments


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.facade.annotation.Route
import com.engineer.imitate.R
import kotlinx.android.synthetic.main.fragment_text_drawable.*


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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        edit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                text.text = s
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })
    }

}
