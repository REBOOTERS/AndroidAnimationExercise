package home.smart.fly.animations.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2

/**
 * Created by Radoslav Yankov on 29.06.2018
 * radoslavyankov@gmail.com
 */

typealias BindingClosure<T> = (View.(item: T, position: Int) -> Unit)

/**
 * Dynamic list bind function. It should be followed by one or multiple .map calls.
 * @param items - Generic list of the items to be displayed in the list
 */
fun <T> RecyclerView.bind(items: List<T>): FastListAdapter<T> {
    layoutManager = LinearLayoutManager(context)
    return FastListAdapter(items.toMutableList(), this)
}

/**
 * ViewPager2 update
 * Dynamic list bind function. It should be followed by one or multiple .map calls.
 * @param items - Generic list of the items to be displayed in the list
 */
fun <T> ViewPager2.bind(items: List<T>): FastListAdapter<T> {
    return FastListAdapter(items.toMutableList(), vpList = this)
}

/**
 * Simple list bind function.
 * @param items - Generic list of the items to be displayed in the list
 * @param singleLayout - The layout that will be used in the list
 * @param singleBind - The "binding" function between the item and the layout. This is the standard "bind" function in traditional ViewHolder classes. It uses Kotlin Extensions
 * so you can just use the XML names of the views inside your layout to address them.
 */
fun <T> RecyclerView.bind(items: List<T>, @LayoutRes singleLayout: Int = 0, singleBind: BindingClosure<T>): FastListAdapter<T> {
    layoutManager = LinearLayoutManager(context)
    return FastListAdapter(items.toMutableList(), this
    ).map(singleLayout, { item: T, position: Int -> true }, singleBind)
}

/**
 * ViewPager2 update
 * Simple list bind function.
 * @param items - Generic list of the items to be displayed in the list
 * @param singleLayout - The layout that will be used in the list
 * @param singleBind - The "binding" function between the item and the layout. This is the standard "bind" function in traditional ViewHolder classes. It uses Kotlin Extensions
 * so you can just use the XML names of the views inside your layout to address them.
 */
fun <T> ViewPager2.bind(items: List<T>, @LayoutRes singleLayout: Int = 0, singleBind: BindingClosure<T>): FastListAdapter<T> {
    return FastListAdapter(items.toMutableList(), vpList = this
    ).map(singleLayout, { item: T, position: Int -> true }, singleBind)
}

/**
 * Updates the list using DiffUtils.
 * @param newItems the new list which is to replace the old one.
 *
 * NOTICE: The comparator currently checks if items are literally the same. You can change that if you want,
 * by changing the lambda in the function
 */
fun <T> RecyclerView.update(newItems: List<T>) {
    (adapter as? FastListAdapter<T>)?.update(newItems) { o, n, _ -> o == n }
}

/**
 * ViewPager2 update
 * Updates the list using DiffUtils.
 * @param newItems the new list which is to replace the old one.
 *
 * NOTICE: The comparator currently checks if items are literally the same. You can change that if you want,
 * by changing the lambda in the function
 */
fun <T> ViewPager2.update(newItems: List<T>) {
    (adapter as? FastListAdapter<T>)?.update(newItems) { o, n, _ -> o == n }
}

open class FastListAdapter<T>(private var items: MutableList<T>, private var list: RecyclerView? = null, private var vpList: ViewPager2? = null)
    : RecyclerView.Adapter<FastListViewHolder<T>>() {

    init {
        if (vpList != null && list != null)
            throw IllegalArgumentException("You can only use either the Recycler(list) or the Pager(vpList)")
        if (vpList == null && list == null)
            throw IllegalArgumentException("You have to use either the Recycler(list) or the Pager(vpList)")

    }

    private inner class BindMap(val layout: Int, var type: Int = 0, val bind: BindingClosure<T>, val predicate: (item: T, position: Int) -> Boolean) {
        constructor(lf: LayoutFactory, type: Int = 0, bind: BindingClosure<T>, predicate: (item: T, position: Int) -> Boolean) : this(0, type, bind, predicate) {
            layoutFactory = lf
        }

        var layoutFactory: LayoutFactory? = null
    }

    private var bindMap = mutableListOf<BindMap>()
    private var typeCounter = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FastListViewHolder<T> {
        return bindMap.first { it.type == viewType }.let {
            it.layoutFactory?.let {
                return FastListViewHolder(it.createView(parent, viewType), viewType)
            } ?: run {
                return FastListViewHolder(LayoutInflater.from(parent.context).inflate(it.layout,
                    parent, false), viewType)
            }
        }
    }

    override fun onBindViewHolder(holder: FastListViewHolder<T>, position: Int) {
        val item = items.get(position)
        holder.bind(item, position, bindMap.first { it.type == holder.holderType }.bind)
    }

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int) = try {
        bindMap.first { it.predicate(items[position], position) }.type
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
    fun map(@LayoutRes layout: Int, predicate: (item: T, position: Int) -> Boolean, bind: BindingClosure<T>): FastListAdapter<T> {
        bindMap.add(BindMap(layout, typeCounter++, bind, predicate))
        list?.adapter = this
        vpList?.adapter = this
        return this
    }

    /**
     * The function used for mapping types to layouts
     * @param layoutFactory - factory that creates the view for this adapter
     * @param predicate - Function used to sort the items. For example, a Type field inside your items class with different values for different types.
     * @param bind - The "binding" function between the item and the layout. This is the standard "bind" function in traditional ViewHolder classes. It uses Kotlin Extensions
     * so you can just use the XML names of the views inside your layout to address them.
     */
    fun map(layoutFactory: LayoutFactory, predicate: (item: T, position: Int) -> Boolean, bind: BindingClosure<T>): FastListAdapter<T> {
        bindMap.add(BindMap(layoutFactory, typeCounter++, bind, predicate))
        list?.adapter = this
        vpList?.adapter = this
        return this
    }

    /**
     * Sets up a layout manager for the recycler view.
     */
    fun layoutManager(manager: RecyclerView.LayoutManager): FastListAdapter<T> {
        vpList?.let { throw UnsupportedOperationException("layoumanager not needed for ViewPager2") }
        list!!.layoutManager = manager
        return this
    }

    fun update(newList: List<T>, compare: (T, T, Boolean) -> Boolean) {
        val diff = DiffUtil.calculateDiff(object : DiffUtil.Callback() {

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return compare(items[oldItemPosition], newList[newItemPosition], false)
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return compare(items[oldItemPosition], newList[newItemPosition], true)
            }

            override fun getOldListSize() = items.size

            override fun getNewListSize() = newList.size
        })
        if (newList is MutableList)
            items = newList
        else
            items = newList.toMutableList()
        diff.dispatchUpdatesTo(this)
    }

}

interface LayoutFactory {
    fun createView(parent: ViewGroup, type: Int): View
}

class FastListViewHolder<T>(private val containerView: View, val holderType: Int) : RecyclerView.ViewHolder(containerView) {
    fun bind(entry: T, position: Int, func: BindingClosure<T>) {
        containerView.apply {
            func(entry, position)
        }
    }
}