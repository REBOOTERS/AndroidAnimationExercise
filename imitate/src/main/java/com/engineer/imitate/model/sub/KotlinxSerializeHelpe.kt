package com.engineer.imitate.model.sub

import android.util.Log
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

val json = Json {}

@Serializable
class School {

    @Serializable
    class Location {
        var lat = 0f
        var lng = 0f
        override fun toString(): String {
            return "Location{" + "lat=" + lat + ", lng=" + lng + '}'
        }
    }

    var name: String? = null

    var province: String? = null

    var city: String? = null

    var area: String? = null

    var address: String? = null

    var location: Location? = null

    override fun toString(): String {
        return "School{" + "name='" + name + '\'' + ", province='" + province + '\'' + ", city='" + city + '\'' + ", area='" + area + '\'' + ", address='" + address + '\'' + ", location=" + location + '}'
    }
}

@Serializable
data class Schools(
    var id: Int = 0,
    var province: String? = null,
    var schoolList: List<School>? = null,
    var type: Int = 0,
    var code: String? = "aa"
) {


    override fun toString(): String {
        return "Schools(province=$province, schoolList=$schoolList)"
    }
}

fun parseWithKotlinxSerializable(jsonStr: String): List<Schools>? {
    val s = System.currentTimeMillis()

    val list = json.decodeFromString<List<Schools>>(jsonStr)
    Log.e("Coroutines", "parseWithKotlinxSerializable cost ${System.currentTimeMillis() - s}")
    return list
}