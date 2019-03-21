package com.engineer.imitate.activity

import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.engineer.imitate.R
import com.engineer.imitate.util.ViewUtils
import kotlinx.android.synthetic.main.activity_fake_jike.*

class FakeJikeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fake_jike)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ViewUtils.getBitmapFormView(image, this) { bitmap -> temp.setImageBitmap(bitmap) }
        }
    }
}
