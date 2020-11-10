package com.engineer.imitate.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.engineer.imitate.R
import kotlinx.android.synthetic.main.activity_c_l.*

class CLActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_c_l)
        bottom.layoutResource = R.layout.activity_c_l_bottom
        bottom.inflate()
    }
}