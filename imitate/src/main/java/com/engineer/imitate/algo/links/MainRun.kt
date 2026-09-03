package com.engineer.imitate.algo.links

fun main() {
    val a1 = hello1(1, 1)
    val a2 = hello1(1, 1, 1)
    println("a1 = $a1 , a2 = $a2")
}

fun hello1(a: Int, b: Int, c: Int = a + 1): Int {
    return a + b + c;
}