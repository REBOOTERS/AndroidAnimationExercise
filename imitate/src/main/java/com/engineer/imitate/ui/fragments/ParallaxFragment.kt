package com.engineer.imitate.ui.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.HORIZONTAL
import com.alibaba.android.arouter.facade.annotation.Route
import com.engineer.imitate.R
import com.engineer.imitate.ui.list.adapter.ParallaxAdapter

/**
 * A simple [Fragment] subclass.
 *
 */
@Route(path = "/anim/parallax")
class ParallaxFragment : Fragment() {
    private lateinit var rvContent: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_parallax, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvContent = view.findViewById(R.id.rvContent)
        rvContent.apply {
            layoutManager = LinearLayoutManager(context, HORIZONTAL, false)
            adapter = ParallaxAdapter(prepareBackTestData(), prepareFrontTestData())
        }
    }

    fun prepareBackTestData(): List<Int> {
        val data = ArrayList<Int>()
        data.add(R.drawable.fast_furious_back)
        data.add(R.drawable.star_wars_back)
        data.add(R.drawable.game_of_thrones_back)
        data.add(R.drawable.sherlok_back)
        data.add(R.drawable.mult_back)
        return data
    }

    fun prepareFrontTestData(): List<Int> {
        val data = ArrayList<Int>()
        data.add(R.drawable.fast_furious_front)
        data.add(R.drawable.star_wars_front)
        data.add(R.drawable.game_of_thrones_front)
        data.add(R.drawable.sherlok_front)
        data.add(R.drawable.mult_front)
        return data
    }

}
