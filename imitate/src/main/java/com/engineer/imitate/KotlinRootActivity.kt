package com.engineer.imitate

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.engineer.imitate.databinding.ActivityKotlinRootBinding
import com.engineer.imitate.model.FragmentItem
import com.engineer.imitate.ui.activity.ReverseGifActivity
import com.engineer.imitate.util.*
import com.example.cpp_native.app.NativeRoot
import com.gyf.immersionbar.ImmersionBar
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.BezierRadarHeader
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.header.FalsifyHeader
import com.scwang.smart.refresh.header.MaterialHeader
import com.scwang.smart.refresh.layout.api.RefreshHeader
import com.skydoves.transformationlayout.onTransformationStartContainer
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.apache.commons.io.FileUtils
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.util.concurrent.TimeUnit

@Route(path = Routes.INDEX)
@SuppressLint("LogNotTimber")
class KotlinRootActivity : AppCompatActivity() {
    private val TAG = KotlinRootActivity::class.java.simpleName
    private val ORIGINAL_URL = "file:///android_asset/index.html"
    private lateinit var hybridHelper: HybridHelper

    private var currentFragment: Fragment? = null
    private lateinit var mLinearManager: LinearLayoutManager
    private lateinit var mGridLayoutManager: GridLayoutManager
    private lateinit var mLayoutManager: RecyclerView.LayoutManager

    private var adapter: FastListAdapter<FragmentItem>? = null
    private lateinit var viewBinding: ActivityKotlinRootBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        onTransformationStartContainer()
        super.onCreate(savedInstanceState)
        ImmersionBar.with(this).fitsSystemWindows(true).statusBarColor(R.color.colorPrimary).init()
        viewBinding = ActivityKotlinRootBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        setSupportActionBar(viewBinding.toolbar)
        loadView()
        jsonTest()

//        autoStartPage()

        NativeRoot.init()
        NativeRoot.test()
        val patchViewModel = ViewModelProvider(this)[PatchViewModel::class.java]
        patchViewModel.copyFile()

    }

    private fun autoStartPage() {
        val fragment: Fragment = ARouter.getInstance().build("/anim/fresco").navigation(this) as Fragment
        currentFragment = fragment
        viewBinding.contentKotlinRoot.content.visibility = View.VISIBLE
        viewBinding.contentKotlinRoot.index.visibility = View.GONE
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.content, fragment).commit()
    }

    private fun loadView() {
        mLinearManager = LinearLayoutManager(this)
        mGridLayoutManager = GridLayoutManager(this, 2)
        mLayoutManager = mLinearManager
//        if (isNetworkConnected()) {
//            loadWebView()
//        } else {
//        }
        loadRecyclerView()
//        loadWebView()

        viewBinding.gif.setOnClickListener {
//            startActivity(Intent(this, ReverseGifActivity::class.java))

            val bundle = viewBinding.transformationLayout.withView(viewBinding.transformationLayout, "myTransitionName")
            val intent = Intent(this, ReverseGifActivity::class.java)
            intent.putExtra("TransformationParams", viewBinding.transformationLayout.getParcelableParams())
            startActivity(intent, bundle)
        }


    }

    // <editor-fold defaultstate="collapsed" desc="拷贝一些文件到特定目录，不是每个手机都需要">
    /**
     * 拷贝一些文件到特定目录，不是每个手机都需要
     */
    private fun personalCopy() {
        val path = filesDir.absolutePath + "/gif/"
        val fileDir = File(path)
        val files = fileDir.listFiles()
        files?.apply {

            if (fileDir.exists()) {
                val destDir = Environment.getExternalStorageDirectory()
                for (file in this) {
                    Log.e(TAG, ": " + file.absolutePath)
                    Observable.just("").subscribeOn(Schedulers.io()).subscribe {
                        FileUtils.copyFileToDirectory(file, destDir)
                    }
                }
            }

        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="init fragments">
    private fun initList(): MutableList<FragmentItem> {
        return mutableListOf(
            FragmentItem("/anim/entrance", "entrance"),
            FragmentItem("/anim/dependency_injection", "dependency_injection"),
            FragmentItem("/anim/rx_play", "rx_play"),
            FragmentItem("/anim/motion_layout", "motion_layout"),
            FragmentItem("/anim/github", "github features"),
            FragmentItem("/anim/circleLoading", "circle-loading"),
            FragmentItem("/anim/coroutines", "coroutines"),
            FragmentItem("/anim/recycler_view", "RecyclerView"),
            FragmentItem("/anim/slide", "slide"),
            FragmentItem("/anim/drawable_example", "drawable_example"),
            FragmentItem("/anim/elevation", "elevation"),
            FragmentItem("/anim/fresco", "fresco"),
            FragmentItem("/anim/constraint", "constraint animation"),
            FragmentItem("/anim/scroller", "scroller"),
            FragmentItem("/anim/vh_fragment", "vh_fragment"),
            FragmentItem("/anim/parallax", "parallax")
        )
    }
    // </editor-fold>

    private fun loadRecyclerView() {
        viewBinding.contentKotlinRoot.hybrid.animate().alpha(0f).setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                viewBinding.contentKotlinRoot.hybrid.visibility = View.GONE
                viewBinding.contentKotlinRoot.smartRefreshLayout.visibility = View.VISIBLE
            }
        }).start()

        val list = initList()
        adapter =
            viewBinding.contentKotlinRoot.recyclerView.bind(list, R.layout.view_item) { item: FragmentItem, _: Int ->
                val desc = findViewById<TextView>(R.id.desc)
                val path = findViewById<TextView>(R.id.path)
                val more_menu = findViewById<ImageView>(R.id.more_menu)
                val shell = findViewById<ViewGroup>(R.id.shell)
                desc.text = item.name
                path.text = item.path

                path.setOnClickListener {
                    AnimDelegate.apply(context, path, viewBinding.gif, viewBinding.shellRoot)
                }
                more_menu.setOnClickListener {
                    showMenu(more_menu)
                }

                shell.setOnClickListener {
                    viewBinding.gif.hide()

                    val fragment: Fragment = ARouter.getInstance().build(item.path).navigation(context) as Fragment
                    currentFragment = fragment
                    viewBinding.contentKotlinRoot.content.visibility = View.VISIBLE
                    viewBinding.contentKotlinRoot.index.visibility = View.GONE
                    val transaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.content, fragment).commit()
                    updateState()
                }
            }.layoutManager(mLayoutManager)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            viewBinding.contentKotlinRoot.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    Log.e(TAG, "dy==$dy")
                    Log.e(TAG, "是否可以 scroll up==${recyclerView.canScrollVertically(-1)}")
                    Log.e(TAG, "是否可以 scroll down==${recyclerView.canScrollVertically(1)}")
                }
            })
        }
        val heads = arrayListOf<RefreshHeader>(
            ClassicsHeader(this), BezierRadarHeader(this), FalsifyHeader(this), MaterialHeader(this)
        )
        viewBinding.contentKotlinRoot.smartRefreshLayout.autoRefresh()
        viewBinding.contentKotlinRoot.smartRefreshLayout.setRefreshHeader(heads.random())
        viewBinding.contentKotlinRoot.smartRefreshLayout.setRefreshFooter(ClassicsFooter(this))
        viewBinding.contentKotlinRoot.smartRefreshLayout.setOnRefreshListener {
            viewBinding.contentKotlinRoot.smartRefreshLayout.finishRefresh(2000)
        }
    }

    private fun showMenu(view: View) {
        val popupMenu = PopupMenu(this, view, Gravity.END)
        popupMenu.menuInflater.inflate(R.menu.index_setting_menu, popupMenu.menu)
        popupMenu.show()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun loadWebView() {
        viewBinding.contentKotlinRoot.hybrid.animate().alpha(1f).setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                viewBinding.contentKotlinRoot.hybrid.visibility = View.VISIBLE
                viewBinding.contentKotlinRoot.smartRefreshLayout.visibility = View.GONE
            }
        }).start()


        viewBinding.contentKotlinRoot.hybrid.settings.javaScriptEnabled = true
        viewBinding.contentKotlinRoot.hybrid.settings.allowFileAccess = true
        hybridHelper = HybridHelper(this)
        hybridHelper.setOnItemClickListener(SimpleClickListener())
        viewBinding.contentKotlinRoot.hybrid.addJavascriptInterface(hybridHelper, "hybrid")

        viewBinding.contentKotlinRoot.hybrid.loadUrl(ORIGINAL_URL)
    }

    private inner class SimpleClickListener : HybridHelper.OnItemClickListener {
        override fun reload() {
            runOnUiThread {
                loadRecyclerView()
            }
        }

        @SuppressLint("RestrictedApi")
        override fun onClick(fragment: Fragment, title: String) {
            runOnUiThread {
                if (!TextUtils.isEmpty(title)) {
                    setTitle(title)
                }
                viewBinding.contentKotlinRoot.content.visibility = View.VISIBLE
                viewBinding.contentKotlinRoot.index.visibility = View.GONE
                viewBinding.gif.hide()
                currentFragment = fragment
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.content, fragment).commit()
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_kotlin_root, menu)
        return true
    }


    private fun isNightMode(): Boolean {
        val flag = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return flag == Configuration.UI_MODE_NIGHT_YES
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val item = menu?.findItem(R.id.theme_switch)
        if (isNightMode()) {
            item?.setIcon(R.drawable.ic_brightness_low_white_24dp)
            item?.title = "日间模式"
        } else {
            item?.setIcon(R.drawable.ic_brightness_high_white_24dp)
            item?.title = "夜间模式"
        }
        val change = menu?.findItem(R.id.action_change)
        val refresh = menu?.findItem(R.id.refresh)
        change?.isVisible = (viewBinding.contentKotlinRoot.smartRefreshLayout.visibility == View.VISIBLE)
        refresh?.isVisible = (viewBinding.contentKotlinRoot.smartRefreshLayout.visibility == View.VISIBLE)
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            if (viewBinding.contentKotlinRoot.content.visibility == View.VISIBLE) {
                releaseFragment()
            } else {
                finish()
            }
        } else if (item.itemId == R.id.action_switch_view) {
            if (viewBinding.contentKotlinRoot.smartRefreshLayout.visibility == View.VISIBLE) {
                loadWebView()
            } else {
                loadRecyclerView()
            }
            return true
        } else if (item.itemId == R.id.action_change) {
            if (viewBinding.contentKotlinRoot.smartRefreshLayout.visibility == View.VISIBLE) {
                if (viewBinding.contentKotlinRoot.recyclerView.layoutManager == mLinearManager) {
                    mLayoutManager = mGridLayoutManager
                } else {
                    mLayoutManager = mLinearManager
                }
                adapter?.layoutManager(mLayoutManager)?.notifyDataSetChanged()
                return true
            }
        } else if (item.itemId == R.id.theme_switch) {
            if (isNightMode()) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                SpUtil(this).saveBool(SpUtil.KEY_THEME_NIGHT_ON, false)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                SpUtil(this).saveBool(SpUtil.KEY_THEME_NIGHT_ON, true)
            }
//            recreate()
        } else if (item.itemId == R.id.refresh) {
            if (viewBinding.contentKotlinRoot.recyclerView.visibility == View.VISIBLE) {
                viewBinding.contentKotlinRoot.smartRefreshLayout.autoRefresh()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (viewBinding.contentKotlinRoot.content.visibility == View.VISIBLE) {
            releaseFragment()
        } else {
            super.onBackPressed()
        }
    }

    private fun releaseFragment() {
        viewBinding.contentKotlinRoot.content.visibility = View.GONE
        viewBinding.contentKotlinRoot.index.visibility = View.VISIBLE
        viewBinding.gif.show()
        Log.e(TAG, "fragments size = " + supportFragmentManager.fragments.size)
        currentFragment?.let {
            if (supportFragmentManager.fragments.size > 0) {
                supportFragmentManager.beginTransaction().remove(it).commitAllowingStateLoss()
                currentFragment = null
                updateState()
            }
        }

    }

    private fun updateState() {
        Observable.just(1).delay(1, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).doOnNext {
            Log.e(TAG, "updateState: ======================================================\n")
            Log.e(TAG, "fragments size = " + supportFragmentManager.fragments.size)
            supportFragmentManager.fragments.forEach {
                Log.e(
                    TAG, "fragment [ ${it.javaClass.name} ] in activity [ ${it.activity?.javaClass?.simpleName} ]"
                )
            }
            Log.e(TAG, "updateState: ======================================================\n")
        }.subscribe()
    }

    private fun jsonTest() {
        val uri = "https://www.zhihu.com/search?q=%E5%88%A9%E7%89%A9%E6%B5%A6&type=content"
        val parseUri = Uri.parse(uri)

        Log.e(TAG, "type  :      " + parseUri::class.java.canonicalName)
        Log.e(TAG, "query:      ${parseUri.query}")
        Log.e(TAG, "isOpaque:   ${parseUri.isOpaque}")

        val jsonArray = JSONArray()
        for (i in 0..3) {
            val jsonObj = JSONObject()
            jsonObj.put("name", "name_$i")
            jsonArray.put(jsonObj)
        }
        Log.e(TAG, "result ==$jsonArray")
    }


    override fun onDestroy() {
        super.onDestroy()
        viewBinding.contentKotlinRoot.hybrid.removeJavascriptInterface("hybrid")
    }
}
