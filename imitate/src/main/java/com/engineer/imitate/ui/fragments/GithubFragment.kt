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
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Route
import com.engineer.imitate.R
import com.engineer.imitate.ui.adapter.ViewPagerFragmentStateAdapter
import com.engineer.imitate.ui.fragments.subs.FragmentFactory
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_github.*


@Route(path = "/anim/github")
class GithubFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_github, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        FragmentFactory.initData()
        val adapter = ViewPagerFragmentStateAdapter(this)
        pager.adapter = adapter
        TabLayoutMediator(tabs, pager) { tab, position ->
            tab.text = FragmentFactory.list[position].title
        }.attach()

        view.findViewById<TextView>(R.id.under_text_2).setOnClickListener {
            Toast.makeText(context, "under_text_2", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        FragmentFactory.clear()
    }
}
