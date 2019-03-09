package home.smart.fly.animations.ui.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.widget.LinearLayout
import de.hdodenhof.circleimageview.CircleImageView
import home.smart.fly.animations.R
import home.smart.fly.animations.widget.dp2px
import kotlinx.android.synthetic.main.activity_prepare.*


const val TAG_1 = "PrepareActivity"

class PrepareActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prepare)


        input.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                val result = s.toString()
                content.text = result
                content1.text = result
                content2.text = result
                content2.invalidate()
            }

        })

        over_layout_1.layoutDirection = LinearLayout.LAYOUT_DIRECTION_RTL
        over_layout_2.layoutDirection = LinearLayout.LAYOUT_DIRECTION_LTR


        retry()


        var imageWidth =0
        val layoutParams = over_layout_3.layoutParams

        for (i in 0..10) {
            val view = LayoutInflater.from(this).inflate(R.layout.item_image, null)
            val imageView = view.findViewById<CircleImageView>(R.id.image)
            imageView.setImageResource(R.drawable.cat)
            over_layout_3.addView(view)


            val params = imageView.layoutParams
            imageWidth = params.width

            layoutParams.width = imageWidth * 10 - imageWidth / 4 * 9
        }

//        over_layout_3.layoutParams = layoutParams




    }


    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)

    }

    private fun retry() {
        val parmas: LinearLayout.LayoutParams = over_layout_1.layoutParams as LinearLayout.LayoutParams
        Log.e(TAG_1, "params.width==${parmas.width}")
        Log.e(TAG_1, "params.height==${parmas.height}")

        val width = dp2px(36f) * (3 - 3 / 4)
        Log.e(TAG_1, "${dp2px(36f)}")
        val value = 108 *2.5
        Log.e(TAG_1, "value=$value")
        Log.e(TAG_1, "width=${width-value}")


        parmas.width = value.toInt()

        over_layout_1.layoutParams = parmas
        over_layout_2.layoutParams = parmas

        Log.e(TAG_1, "params.width==${parmas.width}")
        Log.e(TAG_1, "params.height==${parmas.height}")
    }
}
