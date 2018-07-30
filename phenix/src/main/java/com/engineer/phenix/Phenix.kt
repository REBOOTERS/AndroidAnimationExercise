package com.engineer.phenix

import android.content.Context
import com.engineer.phenix.bean.ImageBean

/**
 *
 * @author: rookie
 * @date: 2018-07-22 21:07
 * @version V1.0
 */
// https://github.com/aizuzi/FastAdapter
class Phenix {

    private lateinit var context: Context
    private lateinit var datas: MutableList<ImageBean>

    private constructor()

    companion object {
        fun getInstance(): Phenix {
            return Holder.INSTANCE
        }
    }

    private object Holder {
        val INSTANCE = Phenix()
    }

    fun with(context: Context): Phenix {
        this.context = context
        return this
    }

    fun load(datas: MutableList<ImageBean>): ParamBuilder {
        this.datas = datas
        return ParamBuilder(this.context, datas)
    }

    fun getDatas(): List<ImageBean> {
        return datas
    }

}