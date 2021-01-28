package com.engineer.imitate.ui.fragments.subs

import androidx.fragment.app.Fragment

/**
 * @author rookie
 * @since 01-08-2020
 */
object FragmentFactory {

    val list = ArrayList<Item>()

    fun initData() {
        list.add(Item("banner", BannerFragment()))
        list.add(Item("animText", AnimationTextFragment()))
        list.add(Item("shadow", ShadowLayoutFragment()))
        list.add(Item("FitText", FitTextViewFragment()))
        list.add(Item("test", TestFragment()))
    }

    data class Item(val title: String, val fragment: Fragment)

    fun clear() {
        list.clear()
    }
}