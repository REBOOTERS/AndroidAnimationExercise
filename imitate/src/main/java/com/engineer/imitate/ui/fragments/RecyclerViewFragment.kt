package com.engineer.imitate.ui.fragments


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.engineer.imitate.R
import com.engineer.imitate.databinding.FragmentRecyclerViewBinding
import com.engineer.imitate.ui.activity.RVActivity
import com.engineer.imitate.ui.list.adapter.LargeImageAdapter
import com.engineer.imitate.ui.list.adapter.SimpleImageAdapter
import com.engineer.imitate.ui.list.decoration.OverLapDecoration
import com.engineer.imitate.ui.list.layoutmanager.FocusLayoutManager
import com.engineer.imitate.ui.widget.more.DZStickyNavLayouts
import com.engineer.imitate.util.dp2px
import kotlin.math.abs

/**
 * https://github.com/Spikeysanju/ZoomRecylerLayout
 */
@Route(path = "/anim/recycler_view")
class RecyclerViewFragment : Fragment() {
    private lateinit var viewBinding: FragmentRecyclerViewBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentRecyclerViewBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding.tv1.gravity = Gravity.CENTER
        viewBinding.tv2.gravity = Gravity.START or Gravity.CENTER_VERTICAL

        viewBinding.recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val adapter = SimpleImageAdapter(getList())
        viewBinding.recyclerView.addItemDecoration(OverLapDecoration(context))
        viewBinding.recyclerView.adapter = adapter

        val _context = context ?: return

        val focusLayoutManager = FocusLayoutManager.Builder()
            .layerPadding(_context.dp2px(14f))
            .normalViewGap(_context.dp2px(14f))
            .focusOrientation(FocusLayoutManager.FOCUS_LEFT)
            .isAutoSelect(true)
            .maxLayerCount(3)
            .setOnFocusChangeListener { focusdPosition, lastFocusdPosition -> }
            .build()

        val emojiStr = String(Character.toChars(Integer.parseInt("1F5F3", 16)))
        viewBinding.emoji.setText("emoji $emojiStr")
        Toast.makeText(context, "value is ${viewBinding.text2.text}", Toast.LENGTH_SHORT).show()

        viewBinding.recyclerView1.layoutManager = focusLayoutManager
        viewBinding.recyclerView1.adapter = LargeImageAdapter(getList())

        viewBinding.recyclerView2.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        viewBinding.recyclerView2.adapter = LargeImageAdapter(getList().subList(0, 3))

        viewBinding.headHomeLayout.setOnStartActivity(object : DZStickyNavLayouts.OnStartActivityListener {
            override fun onStart() {
                viewBinding.drawerLayout.openDrawer(GravityCompat.END)
            }
        })

        viewBinding.webView.settings?.apply {
            domStorageEnabled = true
            javaScriptEnabled = true
        }
        viewBinding.webView.loadUrl("https://ddadaal.me/")

        viewBinding.drawerLayout.addDrawerListener(object : DrawerLayout.SimpleDrawerListener() {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                viewBinding.slideView.update(1 - slideOffset)
                if (slideOffset < 0.1) {
                    viewBinding.webView.visibility = View.INVISIBLE
                } else {
                    viewBinding.webView.visibility = View.VISIBLE
                }
            }
        })

        for (i in 1..5) {
            val view = LayoutInflater.from(context).inflate(R.layout.image_item, null)
            viewBinding.fragmentLayoutManager.containerVertical.addView(view)
        }


        for (i in 1..5) {
            val view = LayoutInflater.from(context).inflate(R.layout.image_item, null)
            viewBinding.fragmentLayoutManager.containerHorizontal.addView(view)
        }

        viewBinding.fragmentLayoutManager.stackViewLayout1.layoutDirection = View.LAYOUT_DIRECTION_LTR
        viewBinding.fragmentLayoutManager.stackViewLayout2.layoutDirection = View.LAYOUT_DIRECTION_RTL
        var x = 0f
        var y = 0f
        viewBinding.nestedScrollview.setOnTouchListener { v, event ->

            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    x = event.x
                    y = event.y
                }
                MotionEvent.ACTION_MOVE -> {
                    val deltaXOrigin = event.x - x
                    val deltaX = abs(deltaXOrigin)
                    val deltaY = abs(event.y - y)
                    Log.e("tag", "deltaX = $deltaX,deltaY = $deltaY")
                    if (deltaX > deltaY) {
                        if (deltaXOrigin < 0 && deltaX > 100) {
                            viewBinding.drawerLayout.openDrawer(GravityCompat.END)
                        }
                        return@setOnTouchListener true
                    }
                }
            }
            false
        }

        viewBinding.rv.setOnClickListener {
            startActivity(Intent(it.context, RVActivity::class.java))
        }
    }

    // <editor-fold defaultstate="collapsed" desc="prepare datas">
    private fun getList(): MutableList<String> {
        val datas = ArrayList<String>()
        datas.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555581149653&di=5912dd2fe4db77ce303569b3e8f34d7b&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201406%2F08%2F20140608161225_VYVEV.jpeg")
        datas.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555581149653&di=5912dd2fe4db77ce303569b3e8f34d7b&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201406%2F08%2F20140608161225_VYVEV.jpeg")
        datas.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555581149653&di=5912dd2fe4db77ce303569b3e8f34d7b&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201406%2F08%2F20140608161225_VYVEV.jpeg")
        datas.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555581149653&di=5912dd2fe4db77ce303569b3e8f34d7b&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201406%2F08%2F20140608161225_VYVEV.jpeg")
        datas.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555581149653&di=5912dd2fe4db77ce303569b3e8f34d7b&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201406%2F08%2F20140608161225_VYVEV.jpeg")
        datas.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555581149653&di=5912dd2fe4db77ce303569b3e8f34d7b&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201406%2F08%2F20140608161225_VYVEV.jpeg")
        datas.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555581149653&di=5912dd2fe4db77ce303569b3e8f34d7b&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201406%2F08%2F20140608161225_VYVEV.jpeg")
        datas.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555581149653&di=5912dd2fe4db77ce303569b3e8f34d7b&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201406%2F08%2F20140608161225_VYVEV.jpeg")
        datas.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555581149653&di=5912dd2fe4db77ce303569b3e8f34d7b&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201406%2F08%2F20140608161225_VYVEV.jpeg")
        datas.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555581149653&di=5912dd2fe4db77ce303569b3e8f34d7b&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201406%2F08%2F20140608161225_VYVEV.jpeg")
        datas.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555581149653&di=5912dd2fe4db77ce303569b3e8f34d7b&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201406%2F08%2F20140608161225_VYVEV.jpeg")
        datas.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555581149653&di=5912dd2fe4db77ce303569b3e8f34d7b&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201406%2F08%2F20140608161225_VYVEV.jpeg")
        datas.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555581149653&di=5912dd2fe4db77ce303569b3e8f34d7b&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201406%2F08%2F20140608161225_VYVEV.jpeg")
        return datas
    }
    // </editor-fold>


}
