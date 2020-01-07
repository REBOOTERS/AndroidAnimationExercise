/*
 * feature from github
 *
 *   https://github.com/Marksss/InfiniteBanner
 *
 *   https://github.com/r4sh33d/AnimatedCountTextView
 *
 *   https://github.com/lihangleo2/ShadowLayout
 *
 */

package com.engineer.imitate.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.alibaba.android.arouter.facade.annotation.Route
import com.engineer.imitate.R
import com.engineer.imitate.ui.adapter.ViewPagerFragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_github.*


@Route(path = "/anim/github")
class GithubFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_github, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = ViewPagerFragmentStateAdapter(titles.size, this)
        pager.adapter = adapter
        pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tabs.setScrollPosition(position, 0f, false)
            }
        })
        TabLayoutMediator(tabs, pager) { tab, position ->
            tab.text = titles[position]
        }.attach()
    }

    companion object {
        val titles = arrayOf("banner", "animText", "shadow")
    }
}
