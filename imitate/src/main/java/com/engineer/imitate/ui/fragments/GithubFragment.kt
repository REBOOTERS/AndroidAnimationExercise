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
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Route
import com.engineer.imitate.R
import com.engineer.imitate.ui.adapter.BannerAdapter
import com.engineer.imitate.ui.widget.InfiniteBannerView
import com.rd.PageIndicatorView


@Route(path = "/anim/github")
class GithubFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_github, container, false)
    }






}
