package com.engineer.imitate.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import com.engineer.imitate.R
import kotlinx.android.synthetic.main.activity_fake_jike.*

class FakeJikeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fake_jike)

        button.setOnClickListener {
            val color = ContextCompat.getColor(this, R.color.green)
            Log.e("color", "color == $color")
            button.setTextColor(-11944391)
            try {
                ddd()
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
    }

    fun ddd() {

        val runtime = Runtime.getRuntime()
        println("runtime processor = ${runtime.availableProcessors()}")

//        throw NullPointerException()
        throw IncompatibleClassChangeError()
    }
}
