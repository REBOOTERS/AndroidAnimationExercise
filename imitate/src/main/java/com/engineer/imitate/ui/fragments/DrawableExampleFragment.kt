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


/**
 * A simple [Fragment] subclass.
 *
 */
@Route(path = "/anim/drawable_example")
class DrawableExampleFragment : Fragment() {
    private val TAG = "DrawableExampleFragment"
    private var disposable: Disposable? = null
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

        disposable = Observable.intervalRange(1, 100, 0, 300, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                Log.e(TAG, "onViewCreated: it      ==$it")
                value_text.text = "${it} %"
                val percent = it / 100f
                Log.e(TAG, "onViewCreated: percent ==$percent")
                progress_view.setProgress(percent)
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposable?.dispose()
    }
}
