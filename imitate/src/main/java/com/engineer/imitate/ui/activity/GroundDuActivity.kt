package com.engineer.imitate.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.engineer.imitate.databinding.ActivityGroundDuBinding
import org.scilab.forge.jlatexmath.core.AjLatexMath

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


        AjLatexMath.init(this)

        val richText = "[h][center]hi![/center][/h][quote]This is quote[/quote][code]print" +
                "(\"Hello FlexibleRichTextView!\")[/code]Hello FlexibleRichTextView!This " +
                "isLaTeX:$\\e^{pi i} + 1 = 0$"
        viewBinding.richTv.setText(richText)
    }


}