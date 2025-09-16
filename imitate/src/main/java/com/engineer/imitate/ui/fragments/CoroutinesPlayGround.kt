package com.engineer.imitate.ui.fragments


import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.fastjson.JSONObject
import com.engineer.imitate.R
import com.engineer.imitate.model.School
import com.engineer.imitate.model.Schools
import com.engineer.imitate.model.sub.parseWithKotlinxSerializable
import com.engineer.imitate.room.SchoolRepository
import com.engineer.imitate.util.IOTool
import com.engineer.imitate.util.bind
import com.engineer.imitate.util.lg
import com.engineer.imitate.util.toastShort
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*

private const val ARG_PARAM1 = "param1"
private const val TAG = "Coroutines"

/**
 * A simple [Fragment] subclass.
 *
 */
@Route(path = "/anim/coroutines")
class CoroutinesFragment : Fragment() {
    private lateinit var progress: ProgressBar
    private lateinit var school_view_pager2: ViewPager2
    private lateinit var school_tab_layout: TabLayout
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_coroutines, container, false)
    }

    @SuppressLint("LogNotTimber")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        useRx {
////            parseWithFastJson(it)
//            parseWithGson(it)
//        }

//        usCoroutine {
//            parseWithGson(it)
//        }
        progress = view.findViewById(R.id.progress)
        school_view_pager2 = view.findViewById(R.id.school_view_pager2)
        school_tab_layout = view.findViewById(R.id.school_tab_layout)
        useCoroutine2 { parseWithFastJson(it) }
        useCoroutine2 { parseWithFastJson1(it) }
        useCoroutine2 { parseWithGson(it) }
        useCoroutine2 { parseWithMoshi(it) }
        useCoroutine2 { parseWithKotlinxSerializable(it) }
    }

    private fun useCoroutine2(block: (String) -> Unit) {
        Thread.currentThread().name.lg(TAG)
        CoroutineScope(Dispatchers.IO).launch {
            Thread.currentThread().name.lg(TAG)
            val start = System.currentTimeMillis()
            val json = IOTool.readStrFromAssets("school.json", context)
            Log.e(TAG, "read json cost ${System.currentTimeMillis() - start}")
            val list = block(json)
//            saveToRoom(list)
            withContext(Dispatchers.Main) {
//                Thread.currentThread().name.lg(TAG)
////                setUpPager(list)
                progress.visibility = View.GONE
            }
        }
    }

    private fun usCoroutine(block: (String) -> List<Schools>?) {
        context.toastShort("use coroutine")
        "1".lg(TAG)
        val coroutineScope = CoroutineScope(Dispatchers.Main)
        "5".lg(TAG)
        coroutineScope.launch {
            "2".lg(TAG)
            val list = readJsonAndParse(block)
            setUpPager(list)
            progress.visibility = View.GONE
            "4".lg(TAG)
        }
        "3".lg(TAG)
    }

    private suspend fun readJsonAndParse(block: (String) -> List<Schools>?): List<Schools>? {
        var list: List<Schools>?
        withContext(Dispatchers.IO) {
            val start = System.currentTimeMillis()
            val json = IOTool.readStrFromAssets("school.json", context)
            Log.e(TAG, "read json cost ${System.currentTimeMillis() - start}")
            list = block(json)
        }
        return list
    }

    @SuppressLint("CheckResult,LogNotTimber")
    private fun useRx(block: (String) -> List<Schools>?) {
        Observable.create<List<Schools>> {
            val start = System.currentTimeMillis()
            val json = IOTool.readStrFromAssets("school.json", context)
            Log.e(TAG, "read json cost ${System.currentTimeMillis() - start}")
            val list = block(json)
            if (list != null) {
                it.onNext(list)
                saveToRoom(list)
                it.onComplete()
            } else {
                it.onError(Throwable("list is null"))
                it.onComplete()
            }

        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
            setUpPager(it)
            progress.visibility = View.GONE
        }, {
            it.printStackTrace()
            progress.visibility = View.GONE
        })
    }

    private fun saveToRoom(list: List<Schools>?) {
        context?.let {
            val schoolRepository = SchoolRepository(it)
            list?.forEach { sc ->
                schoolRepository.insert(sc)
            }
        }
    }

    private fun setUpPager(it: List<Schools>?) {
        val fragments = ArrayList<Item>()
        it?.forEach { sc ->
            sc.schoolList?.let {
                val item = Item(sc.province ?: "", ListFragment.newInstance(ArrayList(it)), it.size)
                fragments.add(item)
            }
        }
        val adapter = ViewPager2FragmentStateAdapter(this, fragments)
        school_view_pager2.adapter = adapter
        school_tab_layout.tabMode = TabLayout.MODE_SCROLLABLE
        school_tab_layout.isTabIndicatorFullWidth = false
        TabLayoutMediator(school_tab_layout, school_view_pager2) { tab, position ->
            tab.text = fragments[position].title + "(${fragments[position].size})"
        }.attach()
    }
}

class ViewPager2FragmentStateAdapter(fragment: Fragment, val fragments: ArrayList<Item>) :
    FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position].fragment
    }

}

class ListFragment : Fragment() {
    companion object {
        fun newInstance(list: ArrayList<School>): ListFragment {
            val fragment = ListFragment()
            val bundle = Bundle()
            bundle.putParcelableArrayList(ARG_PARAM1, list)
            fragment.arguments = bundle
            return fragment
        }
    }

    private var datas = ArrayList<School>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            datas = it.getParcelableArrayList<School>(ARG_PARAM1) as ArrayList<School>
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return RecyclerView(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (view is RecyclerView) {
            view.bind(datas, R.layout.view_item) { value: School, _: Int ->
                val desc = findViewById<TextView>(R.id.desc)
                val path = findViewById<TextView>(R.id.path)
                desc.text = value.name
                path.text = value.address
            }.layoutManager(LinearLayoutManager(context))
        }
    }
}

data class Item(val title: String, val fragment: Fragment, val size: Int)

private fun parseWithFastJson(json: String): List<Schools>? {
    val s = System.currentTimeMillis()
    val list = JSONObject.parseArray(json, Schools::class.java)
    Log.e(TAG, "parseWithFastJson cost ${System.currentTimeMillis() - s}")
    return list
}

private fun parseWithFastJson1(json: String): List<com.engineer.imitate.model.java.Schools>? {
    val s = System.currentTimeMillis()
    val list = JSONObject.parseArray(json, com.engineer.imitate.model.java.Schools::class.java)
    Log.e(TAG, "parseWithFastJson1 cost ${System.currentTimeMillis() - s}")
    return list
}

private fun parseWithGson(json: String): List<Schools>? {
    val s = System.currentTimeMillis()
    val gson = Gson()
    val list: List<Schools> = gson.fromJson(json, object : TypeToken<List<Schools>>() {}.type)
    Log.e(TAG, "parseWithGson cost ${System.currentTimeMillis() - s}")
    return list
}

private fun parseWithMoshi(json: String) :List<Schools>? {
    val s = System.currentTimeMillis()

    val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory()) // 支持 Kotlin 数据类
        .build()
    val adapter = moshi.adapter<List<Schools>>(Types.newParameterizedType(List::class.java, Schools::class.java))
    val list = adapter.fromJson(json)
    Log.e(TAG, "parseWithMoshi cost ${System.currentTimeMillis() - s}")
    return list
}