package com.engineer.imitate

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.engineer.fastlist.bind
import com.engineer.imitate.module.FragmentItem
import com.engineer.imitate.util.StreamUtils
import com.engineer.imitate.util.isNetworkConnected
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java8.util.Optional
import kotlinx.android.synthetic.main.activity_kotlin_root.*
import kotlinx.android.synthetic.main.content_kotlin_root.*
import kotlinx.android.synthetic.main.view_item.view.*

@Route(path = Routes.INDEX)
class KotlinRootActivity : AppCompatActivity() {
    private val TAG = KotlinRootActivity::class.java.simpleName
    private val BASE_URL = "index.html"
    private val ORIGINAL_URL = "file:///android_asset/index.html"
    private lateinit var hybridHelper: HybridHelper

    private lateinit var transaction: FragmentTransaction
    private lateinit var currentFragment: Fragment
    private lateinit var mLinearManager: LinearLayoutManager
    private lateinit var mGridLayoutManager: GridLayoutManager
    private lateinit var mLayoutManager: RecyclerView.LayoutManager


    private lateinit var disposable: Disposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin_root)
        setSupportActionBar(toolbar)
        loadView()
    }

    private fun loadView() {
        mLinearManager = LinearLayoutManager(this)
        mGridLayoutManager = GridLayoutManager(this, 2)
        mLayoutManager = mGridLayoutManager
        if (isNetworkConnected()) {
            loadWebView()
        } else {
            loadRecyclerView()
        }
    }

    private fun loadRecyclerView() {
        hybrid.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
        val list = listOf(
                FragmentItem("/anim/circleLoading", "circle-loading"),
                FragmentItem("/anim/linktextview", "link-textview"),
                FragmentItem("/anim/slide", "slide"),
                FragmentItem("/anim/bottomsheet", "bottomsheet"),
                FragmentItem("/anim/drag", "drag"),
                FragmentItem("/anim/layout_manager", "layout_manager"),
                FragmentItem("/anim/textDrawable", "textDrawable"),
                FragmentItem("/anim/elevation", "elevation"),
                FragmentItem("/anim/fresco", "fresco"),
                FragmentItem("/anim/entrance", "entrance"),
                FragmentItem("/anim/constraint", "constraint animation"),
                FragmentItem("/anim/scroller", "scroller")
        )
        recyclerView.bind(list, R.layout.view_item) { item: FragmentItem ->
            desc.text = item.name
            path.text = item.path
            shell.setOnClickListener {
                val fragment: Fragment = ARouter.getInstance().build(item.path).navigation(context) as Fragment
                currentFragment = fragment
                content.visibility = View.VISIBLE
                index.visibility = View.GONE
                transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.content, fragment).commit()
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
        hybrid.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
        hybrid.settings.javaScriptEnabled = true
        hybrid.settings.allowFileAccess = true
        hybridHelper = HybridHelper(this)
        hybridHelper.setOnItemClickListener(SimpleClickListener())
        hybrid.addJavascriptInterface(hybridHelper, "hybrid")


        disposable = Observable.create<String> { emitter ->
            kotlin.run {
                Optional.ofNullable(assets.open(BASE_URL))
                        .ifPresentOrElse({
                            val result = StreamUtils.readFully(it)
                            emitter.onNext(result)
                            emitter.onComplete()
                        }, { emitter.onError(Throwable("input is null")) })
            }
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    // 内容包含远程 JS 无法正常加载 TODO 修改 jquery mobile 为本地
//                    hybrid.loadDataWithBaseURL("", it, "text/html", "utf-8", null)
                    hybrid.loadUrl(ORIGINAL_URL)
                },
                        {
                            Log.e("tag", it.toString())
                            loadRecyclerView()
                        })


    }

    private inner class SimpleClickListener : HybridHelper.OnItemClickListener {
        override fun onClick(fragment: Fragment, title: String) {
            runOnUiThread {
                if (!TextUtils.isEmpty(title)) {
                    setTitle(title)
                }
                content.visibility = View.VISIBLE
                index.visibility = View.GONE
                currentFragment = fragment
                transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.content, fragment).commit()
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_kotlin_root, menu)
        return true
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
                loadRecyclerView()
            }
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
        if (!transaction.isEmpty) {
            transaction.remove(currentFragment)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!disposable.isDisposed) {
            disposable.dispose()
        }
    }

}
