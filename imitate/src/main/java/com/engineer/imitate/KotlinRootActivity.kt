package com.engineer.imitate

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import kotlinx.android.synthetic.main.activity_kotlin_root.*
import kotlinx.android.synthetic.main.content_kotlin_root.*

@Route(path=Routes.INDEX)
class KotlinRootActivity : AppCompatActivity() {

    private val BASE_URL = "file:///android_asset/index.html"
    private lateinit var hybridHelper: HybridHelper

    private lateinit var transaction:FragmentTransaction
    private lateinit var currentFragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin_root)
        setSupportActionBar(toolbar)


        hybrid.settings.javaScriptEnabled =true
        hybrid.settings.allowFileAccess = true
        hybridHelper = HybridHelper(this)
        hybridHelper.setOnItemClickListener(SimpleClickListener())
        hybrid.addJavascriptInterface(hybridHelper,"hybrid")
        hybrid.loadUrl(BASE_URL)


    }

    private inner class SimpleClickListener:HybridHelper.OnItemClickListener{
        override fun onClick(fragment: Fragment,title:String) {
            runOnUiThread {
                if (!TextUtils.isEmpty(title)) {
                    setTitle(title)
                }
                content.visibility = View.VISIBLE
                index.visibility = View.GONE
                currentFragment = fragment
                transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.content,fragment).commit()
            }
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            if (content.visibility == View.VISIBLE) {
                releaseFragment()
            } else {
                finish()
            }

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (content.visibility == View.VISIBLE) {
            releaseFragment()
        }else{
            super.onBackPressed()

        }
    }

    private fun releaseFragment(){
        content.visibility = View.GONE
        index.visibility = View.VISIBLE
        if(transaction!=null && !transaction.isEmpty){
            transaction.remove(currentFragment)
        }
    }

}
