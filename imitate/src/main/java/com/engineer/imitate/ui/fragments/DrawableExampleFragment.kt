package com.engineer.imitate.ui.fragments


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Route
import com.engineer.imitate.R
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
