package com.engineer.imitate.ui.list.adapter

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

/**
 *
 * @authro: Rookie
 * @since: 2019-04-21
 */
class SimpleArrayAdapter : RecyclerView.Adapter<SimpleArrayAdapter.MyHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val textView = TextView(parent.context)
        textView.setPadding(10, 5, 10, 5)
        return MyHolder(textView)
    }

    override fun getItemCount(): Int {
        return 40 //To change body of created functions use File | Settings | File Templates.
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val textView = holder.itemView as TextView
        textView.text = "position===================$position"
    }

    inner class MyHolder(view: View) : RecyclerView.ViewHolder(view)
}