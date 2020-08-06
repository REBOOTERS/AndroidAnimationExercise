package com.engineer.imitate.ui.activity.fragmentmanager

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.DragEvent
import android.view.View
import android.view.ViewTreeObserver
import androidx.fragment.app.Fragment
import com.engineer.imitate.R
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_content.*
import java.util.concurrent.TimeUnit

class ContentActivity : AppCompatActivity() {
    private lateinit var fragment: Fragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content)

        replace.setOnClickListener {
            fragment = BlinkFragment.newInstance("1", "2")
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_content, fragment, "Blink")
                .commitAllowingStateLoss()
            updateState()
        }

        add.setOnClickListener {
            fragment = TabLayoutsFragment.newInstance("1", "2")
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_content, fragment, "Blink")
                .commitAllowingStateLoss()
            updateState()
        }
        remove.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .remove(fragment)
                .commitAllowingStateLoss()
            updateState()
        }
        add.performClick()
        updateState()

        viewTreeListener()

    }

    private fun viewTreeListener() {
        val TAG = "viewTree"
        replace.viewTreeObserver.addOnDrawListener(object : ViewTreeObserver.OnDrawListener {
            override fun onDraw() {
                Log.d(TAG, "onDraw() called")
                viewInfo(replace)
            }
        })
        replace.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                Log.d(TAG, "onPreDraw() called")
                viewInfo(replace)
                return true
            }
        })
        replace.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                Log.d(TAG, "onGlobalLayout() called")
                viewInfo(replace)
            }
        })
        replace.viewTreeObserver.addOnGlobalFocusChangeListener(object :
            ViewTreeObserver.OnGlobalFocusChangeListener {
            override fun onGlobalFocusChanged(oldFocus: View?, newFocus: View?) {
                Log.d(
                    TAG,
                    "onGlobalFocusChanged() called with: oldFocus = $oldFocus, newFocus = $newFocus"
                )
            }
        })
    }

    private fun viewInfo(view: View) {
        val TAG = "viewTree"
        Log.d(
            TAG, "width=${view.width},height=${view.height}," +
                    "left=${view.left},top=${view.top}," +
                    "right=${view.right},bottom=${view.bottom}"
        )
    }

    @SuppressLint("CheckResult")
    private fun updateState() {
        Observable.just(1)
            .delay(1, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                supportFragmentManager.fragments.forEach {
                    Log.e("ContentActivity", "updateState: " + it.activity?.javaClass?.name)
                    Log.e("ContentActivity", "updateState: " + it.javaClass.name)

                }
                Log.e("ContentActivity", "updateState: ===========================\n")
                if (supportFragmentManager.fragments.size > 1) {
                    replace.isEnabled = false
                    add.isEnabled = false
                    remove.isEnabled = true
                } else {
                    replace.isEnabled = true
                    add.isEnabled = true
                    remove.isEnabled = false
                }
            }


    }
}