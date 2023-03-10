package com.engineer.imitate.ui.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.engineer.imitate.R
import com.engineer.imitate.ui.list.adapter.SimpleArrayAdapter

/**
 * A simple [Fragment] subclass.
 *
 */
@Route(path = "/anim/vh_fragment")
class VHFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vh, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val rv1: RecyclerView = view.findViewById(R.id.rv1)
        val rv2: RecyclerView = view.findViewById(R.id.rv2)
        val rv3: RecyclerView = view.findViewById(R.id.rv3)
        rv1.layoutManager = LinearLayoutManager(context)
        rv1.adapter = SimpleArrayAdapter()
        rv2.layoutManager = LinearLayoutManager(context)
        rv2.adapter = SimpleArrayAdapter()
        rv3.layoutManager = LinearLayoutManager(context)
        rv3.adapter = SimpleArrayAdapter()
    }

}
