package com.engineer.imitate.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.engineer.imitate.databinding.ActivityGroundDuBinding

class GroundDuActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityGroundDuBinding

    private var show = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityGroundDuBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewBinding.testButton.setOnClickListener {
            if (show) {
                viewBinding.loadingText.hide()
            } else {
                viewBinding.loadingText.show()
            }
            show = !show
        }
    }
}