package com.engineer.imitate.ui.fragments.di

import dagger.Component
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

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