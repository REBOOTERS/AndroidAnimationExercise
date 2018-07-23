package com.engineer.imitate

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.engineer.imitate.R.id.tabs
import com.engineer.imitate.R.id.toolbar
import com.engineer.imitate.fragments.CircleLoadingFragment
import com.engineer.imitate.fragments.EvelationFragment
import com.engineer.imitate.fragments.SlideFragment
import com.engineer.imitate.fragments.TextDrawableFragment
import kotlinx.android.synthetic.main.activity_kotlin.*
@Route(path="/index/kotlin")
class KotlinActivity : AppCompatActivity() {

    private lateinit var mSectionsPagerAdapter: SectionsPagerAdapter

    private lateinit var fragments: MutableList<Fragment>
    private lateinit var titles: MutableList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin)
        toolbar.setTitle(R.string.kotlin)
        setSupportActionBar(toolbar)
        initFragments()
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        // Set up the ViewPager with the sections adapter.
        container.adapter = mSectionsPagerAdapter

        container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))
        tabs.setupWithViewPager(container)
        tabs.tabMode=TabLayout.MODE_SCROLLABLE
    }

    fun initFragments() {
        fragments = ArrayList()
        titles = ArrayList()

        fragments.add(SlideFragment())
        fragments.add(CircleLoadingFragment())
        fragments.add(TextDrawableFragment())
        fragments.add(EvelationFragment())

        titles.add("探探")
        titles.add("Loading")
        titles.add("TextDrawable")
        titles.add("evelation")

        //  https://github.com/quiph/RecyclerView-FastScroller
        //  https://github.com/HJ-Money/MTransition
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_kotlin, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.action_settings) {
            return true
        } else if (id == android.R.id.home) {
            finish()
        }



        return super.onOptionsItemSelected(item)
    }


    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            return fragments[position]
        }

        override fun getCount(): Int {
            // Show 3 total pages.
            return fragments.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return titles[position]
        }
    }

}
