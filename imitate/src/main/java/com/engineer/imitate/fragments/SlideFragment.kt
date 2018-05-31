package com.engineer.imitate.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.engineer.imitate.R
import com.engineer.imitate.adapter.SlideListAdapter
import kotlinx.android.synthetic.main.fragment_slide.*


/**
 * A simple [Fragment] subclass.
 *
 */
class SlideFragment : Fragment() {

    private var datas:MutableList<String> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_slide, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        datas.add("1")
        datas.add("11")
        datas.add("111")
        datas.add("1111")
        datas.add("11111")
        datas.add("111111")

        list.adapter = SlideListAdapter(datas)
        list.layoutManager = LinearLayoutManager(context)

    }









}
