package com.engineer.fastlist

import androidx.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer


/**
 * Dynamic list bind function. It should be followed by one or multiple .map calls.
 * @param items - Generic list of the items to be displayed in the list
 */
fun <T> RecyclerView.bind(items: List<T>): FastListAdapter<T> {
    layoutManager =  LinearLayoutManager(context)
    return FastListAdapter(items.toMutableList(), this)
}

/**
 * Simple list bind function.
 * @param items - Generic list of the items to be displayed in the list
 * @param singleLayout - The layout that will be used in the list
 * @param singleBind - The "binding" function between the item and the layout. This is the standard "bind" function in traditional ViewHolder classes. It uses Kotlin Extensions
 * so you can just use the XML names of the views inside your layout to address them.
 */
fun <T> RecyclerView.bind(items: List<T>, @LayoutRes singleLayout: Int = 0, singleBind: (View.(item: T) -> Unit)): FastListAdapter<T> {
    layoutManager =  LinearLayoutManager(context)
    return FastListAdapter(items.toMutableList(), this
    ).map(singleLayout, { true }, singleBind)
}


/**
 * Updates the list using DiffUtils.
 * @param newItems the new list which is to replace the old one.
 *
 * NOTICE: The comparator currently checks if items are literally the same. You can change that if you want,
 * by changing the lambda in the function
 */
fun <T>  RecyclerView.update(newItems: List<T>) {
    (adapter as? FastListAdapter<T>)?.update(newItems) { o, n -> o == n }
}


open class FastListAdapter<T>(private var items: MutableList<T>, private var list:  RecyclerView
) :  RecyclerView.Adapter<FastListViewHolder<T>>() {

    private inner class BindMap(val layout: Int, var type: Int = 0, val bind: View.(item: T) -> Unit, val predicate: (item: T) -> Boolean)

    private var bindMap = mutableListOf<BindMap>()
    private var typeCounter = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FastListViewHolder<T> {
        return bindMap.first { it.type == viewType }.let {
            FastListViewHolder(LayoutInflater.from(parent.context).inflate(it.layout,
                    parent, false), viewType)
        }
    }

    override fun onBindViewHolder(holder: FastListViewHolder<T>, position: Int) {
        val item = items.get(position)
        holder.bind(item, bindMap.first { it.type == holder.holderType }.bind)
    }

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int) = try {
        bindMap.first { it.predicate(items[position]) }.type
    } catch (e: Exception) {
        0
    }

    /**
     * The function used for mapping types to layouts
     * @param layout - The ID of the XML layout of the given type
     * @param predicate - Function used to sort the items. For example, a Type field inside your items class with different values for different types.
     * @param bind - The "binding" function between the item and the layout. This is the standard "bind" function in traditional ViewHolder classes. It uses Kotlin Extensions
     * so you can just use the XML names of the views inside your layout to address them.
     */
    fun map(@LayoutRes layout: Int, predicate: (item: T) -> Boolean, bind: View.(item: T) -> Unit): FastListAdapter<T> {
        bindMap.add(BindMap(layout, typeCounter++, bind, predicate))
        list.adapter = this
        return this
    }

    /**
     * Sets up a layout manager for the recycler view.
     */
    fun layoutManager(manager:  RecyclerView.LayoutManager): FastListAdapter<T> {
        list.layoutManager = manager
        return this
    }

    fun update(newList: List<T>, compare: (T, T) -> Boolean) {
        val diff = DiffUtil.calculateDiff(object : DiffUtil.Callback() {

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return compare(items[oldItemPosition], newList[newItemPosition])
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return items[oldItemPosition] == newList[newItemPosition]
            }

            override fun getOldListSize() = items.size

            override fun getNewListSize() = newList.size
        })
        items = newList.toMutableList()
        diff.dispatchUpdatesTo(this)
    }

}

class FastListViewHolder<T>(override val containerView: View, val holderType: Int) :  RecyclerView.ViewHolder(containerView), LayoutContainer {
    fun bind(entry: T, func: View.(item: T) -> Unit) {
        containerView.apply {
            func(entry)
        }
    }
}