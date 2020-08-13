package com.engineer.imitate.ui.fragments


import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.fastjson.JSONObject
import com.beust.klaxon.Klaxon
import com.engineer.imitate.R
import com.engineer.imitate.model.School
import com.engineer.imitate.model.Schools
import com.engineer.imitate.util.IOTool
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.list.rados.fast_list.bind
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_coroutines.*
import kotlinx.android.synthetic.main.view_item.view.*
import kotlinx.coroutines.*
import kotlin.coroutines.EmptyCoroutineContext

private const val ARG_PARAM1 = "param1"
private const val TAG = "Coroutines"

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
    }

    @SuppressLint("CheckResult,LogNotTimber")
    private fun useRx(block: (String) -> List<Schools>?) {
        Observable.create<List<Schools>> {
            val start = System.currentTimeMillis()
            val json = IOTool.readStrFromAssets("school.json", context!!)
            Log.e(TAG, "read json cost ${System.currentTimeMillis() - start}")
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
                setUpPager(it)
            }
            .doOnError {
                it.printStackTrace()
            }
            .subscribe()
    }

    private fun setUpPager(it: List<Schools>?) {
        val fragments = ArrayList<Item>()
        it?.forEach { sc ->
            sc.schoolList?.let {
                val item = Item(sc.province ?: "", ListFragment.newInstance(ArrayList(it)))
                fragments.add(item)
            }
        }
        val adapter = ViewPager2FragmentStateAdapter(this, fragments)
        school_view_pager2.adapter = adapter
        school_tab_layout.tabMode = TabLayout.MODE_SCROLLABLE
        school_tab_layout.isTabIndicatorFullWidth = false
        TabLayoutMediator(school_tab_layout, school_view_pager2) { tab, position ->
            tab.text = fragments[position].title
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
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return RecyclerView(context!!)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (view is RecyclerView) {
            view.bind(datas, R.layout.view_item) { value: School, _: Int ->
                desc.text = value.name
                path.text = value.address
            }.layoutManager(LinearLayoutManager(context))
        }
    }
}

data class Item(val title: String, val fragment: Fragment)

private fun parseWithKlaxon(json: String): List<Schools>? {
    val s = System.currentTimeMillis()
    val list = Klaxon().parseArray<Schools>(json)
    Log.e(TAG, "parse json cost ${System.currentTimeMillis() - s}")
    return list
}

private fun parseWithFastJson(json: String): List<Schools>? {
    val s = System.currentTimeMillis()
    val list = JSONObject.parseArray(json, Schools::class.java)
    Log.e(TAG, "parse json cost ${System.currentTimeMillis() - s}")
    return list
}

private fun parseWithGson(json: String): List<Schools>? {
    val s = System.currentTimeMillis()
    val gson = Gson()
    val list: List<Schools> = gson.fromJson(json, object : TypeToken<List<Schools>>() {}.type)
    Log.e(TAG, "parse json cost ${System.currentTimeMillis() - s}")
    return list
}

fun coroutines() = runBlocking(EmptyCoroutineContext) {
    GlobalScope.launch(EmptyCoroutineContext, CoroutineStart.DEFAULT) {
        delay(1000)
        Log.e(TAG, "finish")
    }
}