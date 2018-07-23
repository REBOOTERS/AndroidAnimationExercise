package com.engineer.phenix

import android.content.Context
import com.engineer.phenix.bean.ImageBean

/**
 *
 * @author: zhuyongging
 * @date: 2018-07-22 21:07
 * @version V1.0
 */

class Phenix {

    private lateinit var context: Context


    public fun with(context: Context): Phenix {
        this.context = context
        return this
    }

    public fun load(datas:MutableList<ImageBean>): ParamBuilder{
        return ParamBuilder(this.context,datas)
    }

}