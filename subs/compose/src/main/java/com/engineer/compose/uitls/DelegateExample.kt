package com.engineer.compose.uitls

import kotlin.reflect.KProperty

class Delegate {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): String {
        return "$thisRef, thank you for delegating '${property.name}' to me!"
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
        println("$value has been assigned to '${property.name}' in $thisRef.")
    }
}

class People {
    var name by Delegate()
}

fun main() {
    val people = People()
    println(people.name)
    people.name = "mike"
    println(people.name)
}