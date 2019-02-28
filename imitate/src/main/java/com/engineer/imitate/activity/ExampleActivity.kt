package com.engineer.imitate.activity

import android.content.Context
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.engineer.imitate.R
import kotlinx.android.synthetic.main.activity_example.*

class ExampleActivity : AppCompatActivity() {

    private lateinit var mContext: Context
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_example)
        mContext = this


        val content = "BBBB55556666BBBB"
        val clickContent = "5555"
        val clickContent1 = "6666"


        val spannable = SpannableStringBuilder(content)
        val myClickableSpan = MyClickableSpan()
        val myClickableSpan1 = MyClickableSpan1()

        val index = content.indexOf(clickContent)
        val index1 = content.indexOf(clickContent1)

        spannable.setSpan(myClickableSpan, index,
                index + clickContent.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        spannable.setSpan(myClickableSpan1, index1,
                index1 + clickContent1.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        text_view.text = spannable
        text_view.movementMethod = MyLinkMovementMethod().getInstance()

    }

    inner class MyClickableSpan : ClickableSpan() {

        override fun onClick(widget: View) {

            if (widget is TextView) {
                val textView = widget
                val result = textView.text

                if (result is Spanned) {
                    val start = result.getSpanStart(this)
                    val end = result.getSpanEnd(this)

                    val text = textView.text.subSequence(start, end)

                    Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show()
                }


            }
        }

        override fun updateDrawState(ds: TextPaint) {
            ds.color = Color.RED
        }

    }


    inner class MyClickableSpan1 : ClickableSpan() {

        override fun onClick(widget: View) {

            if (widget is TextView) {
                val textView = widget
                val result = textView.text

                if (result is Spanned) {
                    val start = result.getSpanStart(this)
                    val end = result.getSpanEnd(this)

                    val text = textView.text.subSequence(start, end)

                    Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show()
                }


            }
        }

        override fun updateDrawState(ds: TextPaint) {
            ds.color = Color.GREEN
        }

    }

    inner class MyLinkMovementMethod : LinkMovementMethod() {
        override fun onTouchEvent(widget: TextView?, buffer: Spannable?, event: MotionEvent?): Boolean {
            val action = event!!.action
            if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_DOWN) {
                var x = event.x.toInt()
                var y = event.y.toInt()

                x -= widget!!.getTotalPaddingLeft()
                y -= widget.getTotalPaddingTop()

                x += widget.getScrollX()
                y += widget.getScrollY()

                val layout = widget.getLayout()
                val line = layout.getLineForVertical(y)
                val off = layout.getOffsetForHorizontal(line, x.toFloat())

                // 命中字符起始X坐标
                val charStartX = layout.getPrimaryHorizontal(off).toInt()

                // 单个字符宽度
                var singleCharWidth = 0
                if (widget.getText().length > 0) {
                    singleCharWidth = widget.getPaint().measureText(widget.getText()[0].toString()).toInt()
                }

                if (x <= charStartX + singleCharWidth) {// 命中字符范围内，响应点击
                    val links = buffer!!.getSpans(off, off, ClickableSpan::class.java)

                    if (links.size != 0) {
                        if (action == MotionEvent.ACTION_UP) {
                            links[0].onClick(widget)
                        }
                        return true
                    }
                } else {// 没有命中，消耗事件不处理
                    return true
                }
            }
            return super.onTouchEvent(widget, buffer, event)
        }

        fun getInstance(): MyLinkMovementMethod {

            if (sInstance == null) {
                sInstance = MyLinkMovementMethod()
            }

            return sInstance as MyLinkMovementMethod
        }

        private var sInstance: MyLinkMovementMethod? = null
    }
}
