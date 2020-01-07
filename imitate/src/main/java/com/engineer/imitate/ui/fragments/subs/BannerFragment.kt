package com.engineer.imitate.ui.fragments.subs


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.engineer.imitate.R
import com.engineer.imitate.ui.adapter.BannerAdapter
import com.engineer.imitate.ui.widget.InfiniteBannerView
import com.rd.PageIndicatorView

/**
 * A simple [Fragment] subclass.
 */
class BannerFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_banner, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
    }

    private fun initView(view: View) {
        val infiniteBannerView_0: InfiniteBannerView = view.findViewById(R.id.infinite_banner_0)
        infiniteBannerView_0.adapter = BannerAdapter()
        infiniteBannerView_0.setPageTransformer { view, offset ->
            view.scaleY = 0.8f + 0.2f * offset
            view.alpha = 0.5f + 0.5f * offset
        }
        infiniteBannerView_0.onItemClickListener =
            InfiniteBannerView.OnItemClickListener { view, position ->
                Toast.makeText(
                    context,
                    "" + position,
                    Toast.LENGTH_SHORT
                ).show()
            }

        val infiniteBannerView_1: InfiniteBannerView = view.findViewById(R.id.infinite_banner_1)
        infiniteBannerView_1.adapter = BannerAdapter()
        val pageIndicatorView: PageIndicatorView = view.findViewById(R.id.pageIndicatorView)
        pageIndicatorView.count = 3
        infiniteBannerView_1.addOnPageChangeListener(object :
            InfiniteBannerView.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                pageIndicatorView.selection = position
            }
        })
    }
}
