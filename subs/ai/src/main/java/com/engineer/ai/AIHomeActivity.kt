package com.engineer.ai

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity


class AIHomeActivity : AppCompatActivity() {

    private val pages = arrayOf(
        GanActivity::class.java,
        DigitalClassificationActivity::class.java,
        FastStyleTransActivity::class.java
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val linerLayout = LinearLayout(this)
        linerLayout.orientation = LinearLayout.VERTICAL

        for (page in pages) {
            val button = Button(this)
            button.text = page.simpleName
            button.setOnClickListener {
                startActivity(Intent(this, page))
            }
            linerLayout.addView(button)
        }
        setContentView(linerLayout)
    }


}