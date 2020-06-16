package com.engineer.imitate

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.engineer.imitate.model.FragmentItem
import com.engineer.imitate.ui.activity.ReverseGifActivity
import com.list.rados.fast_list.FastListAdapter
import com.list.rados.fast_list.bind
import com.skydoves.transformationlayout.onTransformationStartContainer
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_kotlin_root.*
import kotlinx.android.synthetic.main.content_kotlin_root.*
import kotlinx.android.synthetic.main.view_item.view.*
import org.apache.commons.io.FileUtils
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

@Route(path = Routes.INDEX)
class KotlinRootActivity : AppCompatActivity() {
    private val TAG = KotlinRootActivity::class.java.simpleName
    private val ORIGINAL_URL = "file:///android_asset/index.html"
    private lateinit var hybridHelper: HybridHelper

    private lateinit var currentFragment: Fragment
    private lateinit var mLinearManager: LinearLayoutManager
    private lateinit var mGridLayoutManager: GridLayoutManager
    private lateinit var mLayoutManager: RecyclerView.LayoutManager

    private var adapter: FastListAdapter<FragmentItem>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        onTransformationStartContainer()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin_root)

        setSupportActionBar(toolbar)
        loadView()
        jsonTest()
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

        gif.setOnClickListener {
//            startActivity(Intent(this, ReverseGifActivity::class.java))

            val bundle = transformationLayout.withView(transformationLayout, "myTransitionName")
            val intent = Intent(this, ReverseGifActivity::class.java)
            intent.putExtra("TransformationParams", transformationLayout.getParcelableParams())
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
                    Observable.just("")
                        .subscribeOn(Schedulers.io())
                        .subscribe {
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
            FragmentItem("/anim/github", "github features"),
            FragmentItem("/anim/pure_3d_shapre", "3D shape"),
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
        hybrid.animate().alpha(0f).setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                hybrid.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
            }
        }).start()

        val list = initList()
        adapter = recyclerView.bind(list, R.layout.view_item) { item: FragmentItem ->
            desc.text = item.name
            path.text = item.path
            shell.setOnClickListener {
                gif.hide()

                val fragment: Fragment =
                    ARouter.getInstance().build(item.path).navigation(context) as Fragment
                currentFragment = fragment
                content.visibility = View.VISIBLE
                index.visibility = View.GONE
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.content, fragment).commit()

                Log.e(TAG, "transaction===" + transaction.isEmpty)
            }
        }.layoutManager(mLayoutManager)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    Log.e(TAG, "dy==$dy")
                    Log.e(TAG, "是否可以 scroll up==${recyclerView.canScrollVertically(-1)}")
                    Log.e(TAG, "是否可以 scroll down==${recyclerView.canScrollVertically(1)}")
                }
            })
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun loadWebView() {
        hybrid.animate().alpha(1f).setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                hybrid.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
            }
        }).start()


        hybrid.settings.javaScriptEnabled = true
        hybrid.settings.allowFileAccess = true
        hybridHelper = HybridHelper(this)
        hybridHelper.setOnItemClickListener(SimpleClickListener())
        hybrid.addJavascriptInterface(hybridHelper, "hybrid")

        hybrid.loadUrl(ORIGINAL_URL)
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
                content.visibility = View.VISIBLE
                index.visibility = View.GONE
                gif.hide()
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

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val item = menu?.findItem(R.id.theme_switch)
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            item?.setIcon(R.drawable.ic_brightness_high_white_24dp)
        } else {
            item?.setIcon(R.drawable.ic_brightness_low_white_24dp)
        }
        val change = menu?.findItem(R.id.action_change)
        change?.isVisible = (recyclerView.visibility == View.VISIBLE)
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            if (content.visibility == View.VISIBLE) {
                releaseFragment()
            } else {
                finish()
            }
        } else if (item.itemId == R.id.action_refresh) {
            if (recyclerView.visibility == View.VISIBLE) {
                loadWebView()
            } else {
                loadRecyclerView()
            }
            return true
        } else if (item.itemId == R.id.action_change) {
            if (recyclerView.visibility == View.VISIBLE) {
                if (recyclerView.layoutManager == mLinearManager) {
                    mLayoutManager = mGridLayoutManager
                } else {
                    mLayoutManager = mLinearManager
                }
                adapter?.layoutManager(mLayoutManager)?.notifyDataSetChanged()
                return true
            }
        } else if (item.itemId == R.id.theme_switch) {
            if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            recreate()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (content.visibility == View.VISIBLE) {
            releaseFragment()
        } else {
            super.onBackPressed()
        }
    }

    private fun releaseFragment() {
        content.visibility = View.GONE
        index.visibility = View.VISIBLE
        gif.show()
        val transaction = supportFragmentManager.beginTransaction()
        if (!transaction.isEmpty) {
            transaction.remove(currentFragment)
        }
        Log.e(TAG, "transaction===" + transaction.isEmpty)
        Log.e(TAG, "transaction===" + supportFragmentManager.fragments.size)


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
        hybrid.removeJavascriptInterface("hybrid")
    }
}
