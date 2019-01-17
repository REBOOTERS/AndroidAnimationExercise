package com.engineer.imitate.activity

import android.content.Context
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import com.engineer.imitate.R
import com.engineer.imitate.adapter.DataAdapter
import com.engineer.imitate.interfaces.SimpleOnTabSelectedListener
import com.engineer.imitate.util.dp2px
import com.engineer.imitate.widget.custom.DragView
import kotlinx.android.synthetic.main.activity_final.*
import kotlinx.android.synthetic.main.fragment_final.view.*

class FinalActivity : AppCompatActivity() {

    /**
     * The [android.support.v4.view.PagerAdapter] that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * [android.support.v4.app.FragmentStatePagerAdapter].
     */
    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null

    //<editor-fold desc="data">
    private val titles = arrayOf(R.string.tab_text_1,
            R.string.tab_text_2, R.string.tab_text_3, R.string.tab_text_4,
            R.string.tab_text_5, R.string.tab_text_6, R.string.tab_text_7, R.string.tab_text_8)
    //</editor-fold>

    private lateinit var context: Context


    private var toolbarHeight = 0.0f

    private var screenH = 0

    private var app_bar_h = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this
        setContentView(R.layout.activity_final)
        setSupportActionBar(toolbar)
        title = ""
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        // Set up the ViewPager with the sections adapter.
        container.adapter = mSectionsPagerAdapter
        container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))

        container.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) {
            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
            }

            override fun onPageSelected(p0: Int) {
                Log.e("TAG", "onPageSelected==$p0")
            }

        })

        tabs.tabMode = TabLayout.MODE_SCROLLABLE
        tabs.setupWithViewPager(container)
        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))

        tabs.addOnTabSelectedListener(object : SimpleOnTabSelectedListener {
            override fun onTabSelected(p0: TabLayout.Tab?) {
                Log.e("TAG", "onTabSelected==" + p0!!.position)
                appbar.setExpanded(false)
            }
        })



        drag_bar.setOnClickListener {
            appbar.setExpanded(false)
        }

        appbar.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            override fun onOffsetChanged(p0: AppBarLayout, p1: Int) {
                Log.e("TAG", "p1==$p1")
                toolbar.setBackgroundColor(ContextCompat.getColor(p0.context, R.color.transparent))


                app_bar_h = p1

                var percent = (100.0f * Math.abs(p1 * 2) / p0.totalScrollRange) / 100.0f
                if (percent > 1) {
                    percent = 1.0f
                }

                toolbar.alpha = 1 - percent

                toolbar_layout.title = ""
                toolbar_up.translationY = toolbarHeight * percent - toolbarHeight
            }
        })

        drag_bar.setOnGuesterActionListener(object : DragView.onGuesterActionListener {
            override fun up() {
                appbar.setExpanded(false)
            }

            override fun down() {
                appbar.setExpanded(true)
            }

        })

        fv.setEnable(true)
        fv.setHeader(image)
        fv.setReadyListener { app_bar_h == 0 }
    }


    override fun onResume() {
        super.onResume()
        toolbarHeight = context.dp2px(48.0f)
        Log.e(TAG, "toolbarHeight=$toolbarHeight")
        toolbar_up.translationY = -toolbarHeight
        toolbar_up.visibility = View.VISIBLE
        Log.e(TAG, "yyyy====" + toolbar_up.translationY)

        val dm = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)
        screenH = dm.heightPixels

        Log.e(TAG, "screenH  =" + screenH)
        Log.e(TAG, "statusbar=" + getStatusBarHeight())

    }

    private fun getStatusBarHeight(): Int {
        val resourcesId = resources.getIdentifier("status_bar_height", "dimen", "android")
        return resources.getDimensionPixelSize(resourcesId)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.menu_final, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        if (id == R.id.action_100) {
            image.visibility = View.GONE
            image1.visibility = View.VISIBLE
            return true
        } else if (id == R.id.action_1000) {
            image1.visibility = View.GONE
            image.visibility = View.VISIBLE
        }

        return super.onOptionsItemSelected(item)
    }


    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1)
        }

        override fun getCount(): Int {
            // Show 3 total pages.
            return titles.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return getString(titles[position])
        }
    }

    //<editor-fold desc="fragment">
    /**
     * A placeholder fragment containing a simple view.
     */
    class PlaceholderFragment : Fragment() {

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                  savedInstanceState: Bundle?): View? {
            val rootView = inflater.inflate(R.layout.fragment_final, container, false)
            val type = arguments!!.getInt(ARG_SECTION_NUMBER)
            val list = rootView.list
            val adapter = DataAdapter(type)
            adapter.setSize(20)
            list.adapter = adapter
            list.layoutManager = LinearLayoutManager(context)
            return rootView
        }

        companion object {
            /**
             * The fragment argument representing the section number for this
             * fragment.
             */
            private val ARG_SECTION_NUMBER = "section_number"

            /**
             * Returns a new instance of this fragment for the given section
             * number.
             */
            fun newInstance(sectionNumber: Int): PlaceholderFragment {
                val fragment = PlaceholderFragment()
                val args = Bundle()
                args.putInt(ARG_SECTION_NUMBER, sectionNumber)
                fragment.arguments = args
                return fragment
            }
        }
    }
    //</editor-fold>
}
