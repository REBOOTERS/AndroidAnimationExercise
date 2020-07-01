package com.engineer.imitate.ui.fragments.subs

import androidx.fragment.app.Fragment

/**
 * @author rookie
 * @since 01-08-2020
 */
object FragmentFactory {

    val list = ArrayList<Item>()

    init {
        list.add(Item("banner", BannerFragment()))
        list.add(Item("animText", AnimationTextFragment()))
        list.add(Item("shadow", ShadowLayoutFragment()))
        list.add(Item("FitText", FitTextViewFragment()))
    }

    data class Item(val title: String, val fragment: Fragment)

}