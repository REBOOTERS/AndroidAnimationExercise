package com.engineer.imitate.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.engineer.imitate.R
import com.engineer.imitate.ui.adapter.BasicShapesAdapter

@Route(path = "/anim/pure_3d_share")
class Pure3DShapesFragment : Fragment() {
    private lateinit var shapeList: RecyclerView

    private val adapter by lazy { BasicShapesAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_basic_shapes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        shapeList = view.findViewById(R.id.shapeList)
        initializeView()
    }

    private fun initializeView() {
        shapeList.adapter = adapter
        shapeList.layoutManager = GridLayoutManager(requireContext(), 3)
//        adapter.rotate = Random.nextBoolean()
    }
}