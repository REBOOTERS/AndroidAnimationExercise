package com.engineer.imitate.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.engineer.imitate.R
import com.engineer.imitate.ui.adapter.BasicShapesAdapter
import kotlinx.android.synthetic.main.fragment_basic_shapes.*

@Route(path = "/anim/pure_3d_shapre")
class Pure3DShapesFragment : Fragment() {

    private val adapter by lazy { BasicShapesAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_basic_shapes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeView()
    }

    private fun initializeView() {
        shapeList.adapter = adapter
        shapeList.layoutManager = GridLayoutManager(requireContext(), 3)
//        adapter.rotate = Random.nextBoolean()
    }
}