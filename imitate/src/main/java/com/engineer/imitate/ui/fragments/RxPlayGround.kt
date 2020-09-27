package com.engineer.imitate.ui.fragments

import android.os.Bundle
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
import kotlinx.android.synthetic.main.fragment_rx_play.*
import java.util.concurrent.TimeUnit

@Route(path = "/anim/rx_play")
class RxPlayGroundFragment : Fragment() {
    private val TAG = "RxPlayGround"
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_rx_play, container, false)
    }

    private var disposable: Disposable? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        disposable = Observable.interval(0, 5, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                content.text = it.toString()


                if (it >= 10) {
                    disposable?.dispose()
                }
                Log.e(TAG, "onViewCreated: it ==${it}")
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposable?.dispose()
    }
}