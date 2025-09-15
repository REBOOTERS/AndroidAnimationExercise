package com.engineer.imitate.ui.fragments.di

import dagger.Component
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton


fun main() {
    testDagger()
}

private fun testDagger() {
    val TAG = "Dagger"

//    val applicationGraph = DaggerApplicationGraph.create()

//    val userRepo1 = applicationGraph.repository()
//    val userRepo2 = applicationGraph.repository()
//    Log.e(TAG, "testDagger: user1 == user2 ${userRepo1 == userRepo2}")
//
//
//    val myGraph = DaggerMyGraph.create()
//    val obj1 = myGraph.getObj()
//    val obj2 = myGraph.getObj()
//    Log.e(TAG, "testDagger: obj1 == obj2 ${obj1 == obj2}")
//    Log.e(TAG, "testDagger: obj.getInfo() = ${obj1.getInfo()}")
}

object Log {
    fun e(tag: String, msg: String) {
        System.err.printf("%s: %s\n", tag, msg)
    }
}

class Day @Inject constructor() {
    fun getDay(): Int {
        return Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
    }
}

class Month(val day: Day) {
    @Inject
    constructor() : this(Day())
}

class Year(private val month: Month) {
    @Inject
    constructor() : this(Month(Day()))

    fun msg(): String {
        return "month = ${month.day.getDay()}"
    }
}


class ObjectA(val name: String) {
    @Inject
    constructor() : this("mike")
}

class ObjectB(val age: Int) {
    @Inject
    constructor() : this(18)
}

@Singleton
class MyObject @Inject constructor(
    private val depParamA: ObjectA,
    private val depParamB: ObjectB,
    private val year: Year
) {
    fun getInfo(): String {
        return "name = ${depParamA.name} , age = ${depParamB.age} , year = ${year.msg()}"
    }
}

@Singleton
@Component
interface MyGraph {
    fun getObj(): MyObject
}