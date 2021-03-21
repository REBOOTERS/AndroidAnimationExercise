package com.engineer.imitate.ui.fragments


import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Route
import com.engineer.imitate.R
import com.engineer.imitate.util.span.span.CenteredImageSpan
import com.engineer.imitate.util.span.span.NoUnderlineClickSpan
import com.engineer.imitate.util.span.span.RoundBgColorSpan
import com.engineer.imitate.util.span.toolkit.TextSpanBuilder
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_text_drawable.*
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger


/**
 * A simple [Fragment] subclass.
 *
 */
@Route(path = "/anim/drawable_example")
class DrawableExampleFragment : Fragment() {
    private val TAG = "DrawableExampleFragment"
    private var disposable: Disposable? = null
    private var count = AtomicInteger()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_text_drawable, container, false)
    }


    private fun setLabel() {
        val label: CharSequence = TextSpanBuilder.create("缩进")
                .leadingMargin(100, 0)
                .append("圆角背景文字色")
                .span(RoundBgColorSpan(-0x22bdb3, -0x1, 20))
                .append("\u0020\u0020")
                .append("居中对齐的图片")
                .span(CenteredImageSpan(context, R.mipmap.ic_launcher))
                .append("点击")
                .span(object : NoUnderlineClickSpan() {
                    override fun onClick(widget: View) {
                        Toast.makeText(context, "点击回调", Toast.LENGTH_SHORT).show()
                    }

                })
                .append("背景色")
                .backgroundColor(Color.BLUE)
                .append("前景色")
                .foregroundColor(-0x100)
                .append("粗体")
                .bold()
                .append("斜体")
                .italic()
                .append("粗斜体")
                .boldItalic()
                .append("删除线")
                .strikeThrough()
                .append("下标")
                .subscript()
                .append("上标")
                .superscript()
                .append("下划线")
                .underline()
                .append("文字缩放")
                .xProportion(2f)
                .append("文字大小")
                .sizeInPx(18)
                .append("URL")
                .url("mailto:1032694760@qq.com?subject=test")
                .build()
        textMessage.setText(label)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setLabel()
        edit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                text.text = s
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

        poll()
    }

    private fun poll() {
        Log.e(TAG, "next : ${count.getAndIncrement()}")
        val base = 10L
//        releaseDisposable()
        disposable = Observable.intervalRange(1, base, 0, 1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    Log.e(TAG, "onViewCreated: it      ==$it")
                    val percent = it * 1f / base
                    Log.e(TAG, "onViewCreated: percent ==$percent")
                    value_text.text = "${percent} %"
                    progress_view.setProgress(percent)
                    if (it.toInt() == base.toInt()) {
                        poll()
                    }
                }
    }

    private fun releaseDisposable() {
        disposable?.let {
            if (it.isDisposed.not()) {
                it.dispose()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e(TAG, "onDestroy() called")
    }

    override fun onDestroyView() {
        Log.e(TAG, "onDestroyView() called ${count}")
        super.onDestroyView()
        releaseDisposable()
    }
}
