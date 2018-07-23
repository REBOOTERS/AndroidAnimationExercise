package com.engineer.phenix

import android.content.Context
import com.engineer.phenix.bean.ImageBean

/**
 *
 * @author: zhuyongging
 * @date: 2018-07-22 21:56
 * @version V1.0
 */

public class ParamBuilder {
    private var context: Context
    private lateinit var datas:MutableList<ImageBean>
    private var index:Int=0

    constructor(context: Context,datas:MutableList<ImageBean>){
        this.context=context
        this.datas.clear()
        this.datas.addAll(datas)
    }

    public fun index(index:Int):ParamBuilder {
        this.index=index
        return this
    }

    public fun show() {
        if (context == null) {
            throw IllegalArgumentException("context is null")
        }

        if (datas != null && datas.size > 0) {
            if (index > datas.size) {
                throw IllegalArgumentException("index oversize")
            }
        }


    }



}