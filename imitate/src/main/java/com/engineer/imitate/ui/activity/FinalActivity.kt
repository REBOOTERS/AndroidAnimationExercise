package com.engineer.imitate.ui.activity

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Outline
import android.os.Bundle
import android.os.Debug
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.engineer.imitate.R
import com.engineer.imitate.databinding.ActivityFinalBinding
import com.engineer.imitate.interfaces.SimpleOnTabSelectedListener
import com.engineer.imitate.ui.list.adapter.DataAdapter
import com.engineer.imitate.util.dp2px
import com.engineer.imitate.util.px2dp
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout

const val TAG = "Final_Activity"

class FinalActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityFinalBinding

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
    private val titles = arrayOf(
        R.string.tab_text_1,
        R.string.tab_text_2,
        R.string.tab_text_3,
        R.string.tab_text_4,
        R.string.tab_text_5,
        R.string.tab_text_6,
        R.string.tab_text_7,
        R.string.tab_text_8
    )
    //</editor-fold>

    private lateinit var context: Context


    private var toolbarHeight = 0.0f

    private var screenH = 0

    private var app_bar_h = 0

    private lateinit var provider: CustomViewOutLineProvider

    private var isExpand = false

    private val listener: ViewTreeObserver.OnGlobalLayoutListener = ViewTreeObserver.OnGlobalLayoutListener {
        Log.e("onLayout", "onLayout")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this
        viewBinding = ActivityFinalBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        setSupportActionBar(viewBinding.toolbar)
        title = ""
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        // Set up the ViewPager with the sections adapter.
        viewBinding.container.adapter = mSectionsPagerAdapter
        viewBinding.container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(viewBinding.tabs))

        viewBinding.container.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) {
            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
            }

            override fun onPageSelected(p0: Int) {
                Log.e("TAG", "onPageSelected==$p0")
            }

        })

        viewBinding.tabs.tabMode = TabLayout.MODE_SCROLLABLE
        viewBinding.tabs.setupWithViewPager(viewBinding.container)
        viewBinding.tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(viewBinding.container))

        viewBinding.tabs.addOnTabSelectedListener(object : SimpleOnTabSelectedListener {
            override fun onTabSelected(p0: TabLayout.Tab?) {
                Log.e("TAG", "onTabSelected==" + p0!!.position)
                viewBinding.appbar.setExpanded(false)
            }
        })

        var marginTopAnim: ValueAnimator? = null
        viewBinding.image.setOnClickListener {
            if (marginTopAnim != null && marginTopAnim?.isRunning != false) {
                return@setOnClickListener
            }


            val params: ConstraintLayout.LayoutParams = viewBinding.layout.layoutParams as ConstraintLayout.LayoutParams


            val params1: ConstraintLayout.LayoutParams = viewBinding.card.layoutParams as ConstraintLayout.LayoutParams

            marginTopAnim = if (isExpand) {
                ValueAnimator.ofInt(dp2px(250f).toInt(), 0)
            } else {
                ValueAnimator.ofInt(dp2px(250f).toInt())
            }


            marginTopAnim?.duration = 500
            marginTopAnim?.addUpdateListener {
                var alpha = it.animatedFraction
                if (isExpand) {
                    alpha = 1.0f - alpha
                }
                viewBinding.card.alpha = alpha
                val value: Int = it.animatedValue as Int

                Log.e("value", "value=======$value")

                params.setMargins(0, value, 0, 0)
                viewBinding.layout.layoutParams = params
                val dp = px2dp(value.toFloat())
                Log.e("value", "dp=======$dp")

//                if (dp > 80) {
//                    params1.height = value
//                    card.layoutParams = params1
//                }


            }
            marginTopAnim?.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationRepeat(animation: Animator) {
                    TODO("Not yet implemented")
                }

                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    isExpand = !isExpand
                }
            })
            marginTopAnim?.start()
        }



        viewBinding.appbar.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            override fun onOffsetChanged(p0: AppBarLayout, p1: Int) {
                Log.e("TAG", "p1==$p1")
                viewBinding.toolbar.setBackgroundColor(ContextCompat.getColor(p0.context, R.color.transparent))


                app_bar_h = p1

                var percent = (100.0f * Math.abs(p1 * 2) / p0.totalScrollRange) / 100.0f
                if (percent > 1) {
                    percent = 1.0f
                }

                viewBinding.toolbar.alpha = 1 - percent

                provider.radius = 100f * (1 - percent * 2)
                viewBinding.round.invalidateOutline()

                viewBinding.toolbarLayout.title = ""
                viewBinding.toolbarUp.translationY = toolbarHeight * percent - toolbarHeight

                viewBinding.round.viewTreeObserver.addOnGlobalLayoutListener(listener)
            }
        })


//        fv.setEnable(true)
//        fv.setHeader(image)
//        fv.setReadyListener { app_bar_h == 0 }


        provider = CustomViewOutLineProvider()
        viewBinding.round.outlineProvider = provider
        viewBinding.round.clipToOutline = true

    }

    override fun onDestroy() {
        super.onDestroy()
        viewBinding.round.viewTreeObserver.removeOnGlobalLayoutListener(listener)
    }

    inner class CustomViewOutLineProvider : ViewOutlineProvider() {

        var radius = 0f

        override fun getOutline(view: View?, outline: Outline?) {
            outline!!.setRoundRect(0, 0, view!!.width, view.height, radius)
        }

    }


    override fun onResume() {
        super.onResume()
        toolbarHeight = context.dp2px(48.0f)
        Log.e(TAG, "toolbarHeight=$toolbarHeight")
        viewBinding.toolbarUp.translationY = -toolbarHeight
        viewBinding.toolbarUp.visibility = View.VISIBLE
        Log.e(TAG, "yyyy====" + viewBinding.toolbarUp.translationY)

        val dm = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)
        screenH = dm.heightPixels

        Log.e(TAG, "screenH  =" + screenH)
        Log.e(TAG, "statusbar=" + getStatusBarHeight())

        Log.e(TAG, "Pss             : " + Debug.getPss())
        Log.e(TAG, "native Heap size: " + Debug.getNativeHeapSize())


    }

    private fun getStatusBarHeight(): Int {
        val resourcesId = resources.getIdentifier("status_bar_height", "dimen", "android")
        return resources.getDimensionPixelSize(resourcesId)
    }


    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/viewBinding.tabs/pages.
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

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
        ): View? {
            val rootView = inflater.inflate(R.layout.fragment_final, container, false)
            val list: RecyclerView = rootView.findViewById(R.id.list)
            val adapter = DataAdapter()
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

    open inner class AnimatorListenerAdapter : Animator.AnimatorListener {
        override fun onAnimationRepeat(animation: Animator) {

        }

        override fun onAnimationEnd(animation: Animator) {
        }

        override fun onAnimationCancel(animation: Animator) {
        }

        override fun onAnimationStart(animation: Animator) {
        }
    }
}
