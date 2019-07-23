package home.smart.fly.animations.recyclerview.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import home.smart.fly.animations.R
import home.smart.fly.animations.recyclerview.DataFactory

/**
 * @author zhuyongging @ Zhihu Inc.
 * @since 07-23-2019
 */
abstract class BaseListFragment : Fragment() {
    var mContext: Context? = null
    protected lateinit var datas: ArrayList<String>
    protected lateinit var adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>
    lateinit var mRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = context
        datas = loadDatas()
    }

    fun loadDatas(): ArrayList<String> {
        return DataFactory.initDefaultData(mContext)
    }

    abstract fun getLayoutResId(): Int

    abstract fun  getCustomAdapter(): RecyclerView.Adapter<*>

    open fun getCustomLayoutManager(): RecyclerView.LayoutManager {
        return LinearLayoutManager(mContext)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutResId(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mRecyclerView = view.findViewById(R.id.recyclerview)
        mRecyclerView.layoutManager = getCustomLayoutManager()
        mRecyclerView.adapter = getCustomAdapter()
    }
}