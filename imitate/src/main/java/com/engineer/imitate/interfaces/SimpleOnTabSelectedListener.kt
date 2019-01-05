package com.engineer.imitate.interfaces

import android.support.design.widget.TabLayout

/**
 *
 * @author: zhuyongging
 * @since: 2019-01-05
 */
interface SimpleOnTabSelectedListener:TabLayout.OnTabSelectedListener {
    override fun onTabReselected(p0: TabLayout.Tab?) {
    }

    override fun onTabUnselected(p0: TabLayout.Tab?) {
    }

    override fun onTabSelected(p0: TabLayout.Tab?) {
    }
}