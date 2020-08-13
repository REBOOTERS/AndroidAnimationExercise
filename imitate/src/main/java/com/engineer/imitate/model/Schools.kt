package com.engineer.imitate.model

class Schools {
    var province: String? = null
    var schoolList: List<School>? = null

    override fun toString(): String {
        return "Schools(province=$province, schoolList=$schoolList)"
    }
}