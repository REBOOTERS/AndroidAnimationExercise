package home.smart.fly.animations.recyclerview

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import home.smart.fly.animations.utils.Tools

/**
 * @author zhuyongging @ Zhihu Inc.
 * @since 07-23-2019
 */
object DataFactory {

    fun initDefaultData(context: Context?): ArrayList<String> {
        val josn = Tools.readStrFromAssets("pics.json", context)
        val gson = Gson()
        return gson.fromJson(josn, object : TypeToken<ArrayList<String>>() {}.type)
    }

    fun initStringData(): ArrayList<String> {
        val datas = ArrayList<String>()
        for (i in 0..100) {
            datas.add("This is $i")
        }
        return datas
    }
}