package com.engineer.imitate.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.engineer.imitate.R
import com.engineer.imitate.adapter.SlideListAdapter
import com.engineer.imitate.widget.view.layoutmanager.ItemTouchHelperCallback
import com.engineer.imitate.widget.view.layoutmanager.OnSlideListener
import com.engineer.imitate.widget.view.layoutmanager.SlideLayoutManager
import kotlinx.android.synthetic.main.fragment_slide.*
import java.util.*


/**
 * A simple [Fragment] subclass.
 *
 */
class SlideFragment : Fragment() {

    private var datas:MutableList<String> = ArrayList()
    private lateinit var mItemTouchHelperCallback:ItemTouchHelperCallback<String>
    private lateinit var mItemTouchHelper: ItemTouchHelper
    private lateinit var mSlideLayoutManager: SlideLayoutManager
    private var mOnSlideListener:SimpleOnSlideListener= SimpleOnSlideListener()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        initData()
        return inflater.inflate(R.layout.fragment_slide, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        list.adapter = SlideListAdapter(datas)
        mItemTouchHelperCallback = ItemTouchHelperCallback(list.adapter, datas)
        mItemTouchHelper = ItemTouchHelper(mItemTouchHelperCallback)
        mSlideLayoutManager=SlideLayoutManager(list,mItemTouchHelper)
        mItemTouchHelper.attachToRecyclerView(list)
        list.layoutManager = mSlideLayoutManager
        mItemTouchHelperCallback.setOnSlideListener(mOnSlideListener)
    }


    private inner class SimpleOnSlideListener:OnSlideListener<String>{
        override fun onSliding(viewHolder: RecyclerView.ViewHolder, ratio: Float, direction: Int) {
        }

        override fun onSlided(viewHolder: RecyclerView.ViewHolder, t: String, direction: Int) {
            datas.add("123223")
        }

        override fun onClear() {
        }

    }

    private fun initData() {
        datas.add("1")
        datas.add("11")
        datas.add("111")
        datas.add("1111")
        datas.add("11111")
        datas.add("111111")
    }









}
