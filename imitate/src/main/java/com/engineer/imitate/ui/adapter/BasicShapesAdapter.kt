package com.engineer.imitate.ui.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.engineer.imitate.R
import com.engineer.imitate.model.Basic
import com.engineer.imitate.util.inflate
import kotlinx.android.synthetic.main.fragment_weather_status_item.view.*
import kotlin.properties.Delegates

class BasicShapesAdapter: RecyclerView.Adapter<BasicShapesAdapter.ViewHolder>() {
    enum class PayloadType {
        ROTATE_CHANGED
    }

    private val collection = emptyArray<Any>()

    internal var rotate: Boolean by Delegates.observable(true) { _, old, new->
        if (old != new) {
            collection.indices.forEach {
                notifyItemChanged(it, PayloadType.ROTATE_CHANGED)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(parent.inflate(R.layout.fragment_weather_status_item))

    override fun getItemCount() = collection.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.bind(collection[position])
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            when (payloads[0] as PayloadType) {
                PayloadType.ROTATE_CHANGED -> {
                    rotate(holder.itemView, rotate)
                }
            }
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        fun bind(shape: World) {
//            with(itemView) {
//                icon.setImageDrawable(shape)
//                rotate(itemView, rotate)
//            }
//        }
    }

    private fun rotate(itemView: View, rotate: Boolean) {
        with(itemView) {
            if (rotate) {
                icon.start()
            } else {
                icon.cancel()
            }
        }
    }
}