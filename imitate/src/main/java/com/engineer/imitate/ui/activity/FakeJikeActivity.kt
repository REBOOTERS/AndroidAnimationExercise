package com.engineer.imitate.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.dovar.dtoast.DToast
import com.engineer.imitate.ImitateApplication
import com.engineer.imitate.R
import kotlinx.android.synthetic.main.activity_fake_jike.*


class FakeJikeActivity : AppCompatActivity() {

    @SuppressLint("LogNotTimber")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fake_jike)
        follow.setOnClickListener {
            animation_border.startAnim()
        }

        button.setOnClickListener {
            val color = ContextCompat.getColor(this, R.color.green)
            Log.e("color", "di    == ${R.color.green}")
            Log.e("color", "color == $color")
            button.setTextColor(-11944391)
            try {
                ddd()
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }

        toast1.setOnClickListener {
            Toast.makeText(it.context, "view.context", Toast.LENGTH_SHORT).show()
        }

        toast2.setOnClickListener {
            Toast.makeText(
                ImitateApplication.application, "application context", Toast.LENGTH_SHORT
            ).show()
        }

        toast3.setOnClickListener {
            DToast.make(it.context)
                .setText(R.id.tv_content_default, "DToast it.context")
                .setGravity(Gravity.BOTTOM or Gravity.CENTER, 0, 30)
                .show()
        }

        toast4.setOnClickListener {
            DToast.make(ImitateApplication.application)
                .setText(R.id.tv_content_default, "DToast application context")
                .setGravity(Gravity.BOTTOM or Gravity.CENTER, 0, 30)
                .show()
        }
    }

    fun ddd() {

        val runtime = Runtime.getRuntime()
        println("runtime processor = ${runtime.availableProcessors()}")

//        throw NullPointerException()
        throw IncompatibleClassChangeError()
    }
}
