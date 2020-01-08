package com.engineer.imitate.ui.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.engineer.imitate.ui.fragments.subs.FragmentFactory

/**
 * @author rookie
 * @since 01-07-2020
 */
class ViewPagerFragmentStateAdapter(activity: Fragment) :
    FragmentStateAdapter(activity) {


    override fun getItemCount(): Int {
        return FragmentFactory.list.size
    }

    override fun createFragment(position: Int): Fragment {
        return FragmentFactory.list[position].fragment
    }
}