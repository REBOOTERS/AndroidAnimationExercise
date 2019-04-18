package com.engineer.imitate.ui.activity

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.engineer.imitate.R
import com.engineer.imitate.util.ViewUtils
import com.engineer.imitate.ui.widget.ShadowStack
import kotlinx.android.synthetic.main.activity_fake_jike.*
import org.jetbrains.annotations.NotNull

class FakeJikeActivity : AppCompatActivity() {

    override fun onCreate(@NotNull savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_fake_jike)

        val shadowStack = ShadowStack<View>(this)

        shadowStack.setTargetView(image)
        shadowStack.setContainer(container)

        val shadowStack1 = ShadowStack<View>(this)
        shadowStack1.setTargetView(text)
        shadowStack1.setContainer(container)

        val shadowStack2 = ShadowStack<View>(this)
        shadowStack2.setTargetView(layout)
        shadowStack2.setContainer(container)

    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ViewUtils.getBitmapFormView(image, this) { bitmap -> temp.setImageBitmap(bitmap) }
        }

        temp1.setImageBitmap(ViewUtils.getBitmapFromView(image))
    }
}
