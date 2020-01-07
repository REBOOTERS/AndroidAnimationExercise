package com.engineer.imitate.ui.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.engineer.imitate.ui.fragments.subs.BannerFragment

/**
 * @author rookie
 * @since 01-07-2020
 */
class ViewPagerFragmentStateAdapter(activity: Fragment) : FragmentStateAdapter(activity) {
    private val fragments = ArrayList<Fragment>()

    init {
        fragments.add(BannerFragment())
    }

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}