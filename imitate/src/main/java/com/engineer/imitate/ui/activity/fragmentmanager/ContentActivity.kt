package com.engineer.imitate.ui.activity.fragmentmanager

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
            fragment = BlinkFragment.newInstance("1", "2")
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_content, fragment, "Blink")
                .commitAllowingStateLoss()
            updateState()
        }
        remove.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .remove(fragment)
                .commitAllowingStateLoss()
            updateState()
        }
        updateState()
    }

    @SuppressLint("CheckResult")
    private fun updateState() {
        Observable.just(1)
            .delay(1,TimeUnit.SECONDS)
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