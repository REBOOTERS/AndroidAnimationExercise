package com.engineer.imitate.ui.fragments


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.facade.annotation.Route

import com.engineer.imitate.R
import kotlinx.coroutines.*
import kotlin.coroutines.EmptyCoroutineContext

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
@Route(path = "/anim/coroutines")
class CoroutinesFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_coroutines, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        GlobalScope.launch( EmptyCoroutineContext, CoroutineStart.DEFAULT) {
            delay(1000)
        }



        GlobalScope.launch {

            delay(1000)
            Log.e("coroutine", " World!")
        }
        Log.e("coroutine", " Hello!")

        Thread.sleep(2000)
    }


    fun coroutines() = runBlocking(EmptyCoroutineContext) {
        GlobalScope.launch(EmptyCoroutineContext,CoroutineStart.DEFAULT) {
            delay(1000)
            Log.e("coroutine","finish")
        }
    }


}
