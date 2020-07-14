package com.engineer.imitate.ui.activity.fragmentmanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.engineer.imitate.R
import kotlinx.android.synthetic.main.activity_content.*

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
        }

        add.setOnClickListener {
            fragment = BlinkFragment.newInstance("1", "2")
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_content, fragment, "Blink")
                .commitAllowingStateLoss()
        }

        remove.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .remove(fragment)
                .commitAllowingStateLoss()
        }
    }
}