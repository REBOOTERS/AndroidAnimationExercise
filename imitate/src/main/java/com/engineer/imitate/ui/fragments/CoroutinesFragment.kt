package com.engineer.imitate.ui.fragments


import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.fastjson.JSONObject
import com.beust.klaxon.Klaxon

import com.engineer.imitate.R
import com.engineer.imitate.model.Schools
import com.engineer.imitate.util.IOTool
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_coroutines.*
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_coroutines, container, false)
    }

    @SuppressLint("LogNotTimber")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        useRx {
//            parseWithFastJson(it)
//            parseWithKlaxon(it)
            parseWithGson(it)
        }


//        GlobalScope.launch( EmptyCoroutineContext, CoroutineStart.DEFAULT) {
//            delay(1000)
//        }
//
//
//
//        GlobalScope.launch {
//
//            delay(1000)
//            Log.e("coroutine", " World!")
//        }
//        Log.e("coroutine", " Hello!")
//
//        Thread.sleep(2000)
    }

    @SuppressLint("CheckResult,LogNotTimber")
    private fun useRx(block: (String) -> List<Schools>?) {
        Observable.create<List<Schools>> {
            val start = System.currentTimeMillis()
            val json = IOTool.readStrFromAssets("school.json", context!!)
            Log.e("Coroutines", "read json cost ${System.currentTimeMillis() - start}")
            println("json is $json")
            val list = block(json)
            if (list != null) {
                it.onNext(list)
                it.onComplete()
            } else {
                it.onError(Throwable("list is null"))
                it.onComplete()
            }

        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete {
                progress.visibility = View.GONE
            }
            .doOnNext {
                it.forEach { school ->
                    Log.e("Coroutines", "province : $school")
                }
            }
            .doOnError {
                it.printStackTrace()
            }
            .subscribe()
    }

    private fun parseWithKlaxon(json: String): List<Schools>? {
        val s = System.currentTimeMillis()
        val list = Klaxon().parseArray<Schools>(json)
        Log.e("Coroutines", "parse json cost ${System.currentTimeMillis() - s}")
        return list
    }

    private fun parseWithFastJson(json: String): List<Schools>? {
        val s = System.currentTimeMillis()
        val list = JSONObject.parseArray(json, Schools::class.java)
        Log.e("Coroutines", "parse json cost ${System.currentTimeMillis() - s}")
        return list
    }

    private fun parseWithGson(json: String): List<Schools>? {
        val s = System.currentTimeMillis()
        val gson = Gson()
        val list: List<Schools> = gson.fromJson(json, object : TypeToken<List<Schools>>() {}.type)
        Log.e("Coroutines", "parse json cost ${System.currentTimeMillis() - s}")
        return list
    }

    fun coroutines() = runBlocking(EmptyCoroutineContext) {
        GlobalScope.launch(EmptyCoroutineContext, CoroutineStart.DEFAULT) {
            delay(1000)
            Log.e("coroutine", "finish")
        }
    }


}
