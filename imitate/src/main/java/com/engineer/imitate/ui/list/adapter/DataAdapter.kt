package com.engineer.imitate.ui.list.adapter

import android.annotation.SuppressLint
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.engineer.imitate.R

/**
 *
 * @author: zhuyongging
 * @since: 2019-01-05
 */
class DataAdapter(private var type: Int) : RecyclerView.Adapter<DataAdapter.MyViewHolder>() {

    private  var size = 100;

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.view_item, p0, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return size
    }

    public fun setSize(size:Int) {
        this.size = size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(p0: MyViewHolder, p1: Int) {

        val type = p1 % 2
        when (type) {
            0 -> p0.shell.setBackgroundResource(R.color.cpb_grey)
            1 -> p0.shell.setBackgroundResource(R.color.cpb_white)
        }
        p0.desc.text = "this is $p1"
        p0.path.text = "$p1"
    }


    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val desc = itemView.findViewById<TextView>(R.id.desc)
        val path = itemView.findViewById<TextView>(R.id.path)
        val shell = itemView.findViewById<ConstraintLayout>(R.id.shell)
    }
}