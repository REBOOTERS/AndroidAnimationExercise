package com.engineer.imitate.ui.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.engineer.imitate.ui.fragments.subs.AnimationTextFragment
import com.engineer.imitate.ui.fragments.subs.BannerFragment
import com.engineer.imitate.ui.fragments.subs.ShadowLayoutFragment

/**
 * @author rookie
 * @since 01-07-2020
 */
class ViewPagerFragmentStateAdapter(private val length: Int, activity: Fragment) :
    FragmentStateAdapter(activity) {
    private val fragments = ArrayList<Fragment>()

    init {
        fragments.add(BannerFragment())
        fragments.add(AnimationTextFragment())
        fragments.add(ShadowLayoutFragment())
    }

    override fun getItemCount(): Int {
        return length
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}