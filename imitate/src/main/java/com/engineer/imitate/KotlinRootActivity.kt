package com.engineer.imitate

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.engineer.fastlist.bind
import com.engineer.imitate.module.FragmentItem
import com.engineer.imitate.util.isNetworkConnected
import kotlinx.android.synthetic.main.activity_kotlin_root.*
import kotlinx.android.synthetic.main.content_kotlin_root.*
import kotlinx.android.synthetic.main.recycler_view_item.view.*

@Route(path = Routes.INDEX)
class KotlinRootActivity : AppCompatActivity() {

    private val BASE_URL = "file:///android_asset/index.html"
    private lateinit var hybridHelper: HybridHelper

    private lateinit var transaction: FragmentTransaction
    private lateinit var currentFragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin_root)
        setSupportActionBar(toolbar)


        loadView()


    }

    private fun loadView() {
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
                FragmentItem("/anim/slide", "slide"),
                FragmentItem("/anim/textDrawable", "textDrawable"),
                FragmentItem("/anim/elevation", "elevation"),
                FragmentItem("/anim/fresco", "fresco")
        )
        recyclerView.bind(list, R.layout.recycler_view_item) { item: FragmentItem ->
            name.text = item.name
            path.text = item.path
                    shell.setOnClickListener {

                        Toast.makeText(context,"here",Toast.LENGTH_SHORT).show()

                        val fragment: Fragment = ARouter
                                .getInstance()
                                .build(item.path)
                                .navigation(context) as Fragment

                        currentFragment =fragment
                        content.visibility = View.VISIBLE
                        index.visibility = View.GONE
                        transaction = supportFragmentManager.beginTransaction()
                        transaction.replace(R.id.content, fragment).commit()
                    }
        }
                .layoutManager(LinearLayoutManager(this))
    }

    private fun loadWebView() {
        hybrid.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
        hybrid.settings.javaScriptEnabled = true
        hybrid.settings.allowFileAccess = true
        hybridHelper = HybridHelper(this)
        hybridHelper.setOnItemClickListener(SimpleClickListener())
        hybrid.addJavascriptInterface(hybridHelper, "hybrid")
        hybrid.loadUrl(BASE_URL)
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
        if (transaction != null && !transaction.isEmpty) {
            transaction.remove(currentFragment)
        }
    }

}
