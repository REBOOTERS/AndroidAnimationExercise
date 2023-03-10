package home.smart.fly.animations.ui.activity

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import home.smart.fly.animations.R
import home.smart.fly.animations.utils.*
import home.smart.fly.animations.widget.OverLapLayout


const val TAG_1 = "PrepareActivity"

class PrepareActivity : AppCompatActivity() {
    private lateinit var content3: TextView

    private val url =
        "http://pic.vjshi.com/2018-04-11/78243894ece50c9b1ad6fa6c211a1bdb/00003.jpg?x-oss-process=style/watermark"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_prepare)
        val webImage: ImageView = findViewById(R.id.webImage)
        Glide.with(this).load(url).into(webImage)
        val color_text: TextView = findViewById(R.id.color_text)
        color_text.setTextColor(Color.parseColor("#ff3366"))

        val up = resources.getDrawable(R.drawable.ic_expand_less_black_24dp)
        val down = resources.getDrawable(R.drawable.ic_expand_more_black_24dp)
        UtilMoreText(color_text, getString(R.string.text_long_chinese), up, down).createImg()

        val more_text_view: TextView = findViewById(R.id.more_text_view)
        MyMoreText.getInstance().getLastIndexForLimit(more_text_view, 1, getString(R.string.text_long_chinese))
        val name: TextView = findViewById(R.id.name)
        val name1: TextView = findViewById(R.id.name1)
        name.maxWidth = 0
        name1.maxWidth = 20
        val input: EditText = findViewById(R.id.input)
        val content: TextView = findViewById(R.id.content)
        val content1: TextView = findViewById(R.id.content1)
        val content2: TextView = findViewById(R.id.content2)
        content3 = findViewById(R.id.content3)
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
                content3.text = result
            }

        })
        val over_layout_1: OverLapLayout = findViewById(R.id.over_layout_1)
        val over_layout_2: OverLapLayout = findViewById(R.id.over_layout_2)
        val over_layout_3: OverLapLayout = findViewById(R.id.over_layout_3)
        over_layout_1.layoutDirection = LinearLayout.LAYOUT_DIRECTION_RTL
        over_layout_2.layoutDirection = LinearLayout.LAYOUT_DIRECTION_LTR

        for (i in 0..10) {
            val view = LayoutInflater.from(this).inflate(R.layout.item_image, null)
            val imageView = view.findViewById<CircleImageView>(R.id.image)
            imageView.setImageResource(R.drawable.cat)
            over_layout_3.addView(view)
        }
        val nested_scrollview: NestedScrollView = findViewById(R.id.nested_scrollview)
        findViewById<View>(R.id.scroll_to).setOnClickListener {
            nested_scrollview.scrollTo(0, 600)
        }

        findViewById<View>(R.id.scroll_by).setOnClickListener {
            nested_scrollview.scrollBy(0, 300)
        }

    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        lg("content3 width is ${content3.measuredWidth}")
        content3.maxWidth = screenWidth - (32 + 24 + 6).dp
    }
}
