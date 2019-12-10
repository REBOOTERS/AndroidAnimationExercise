package com.engineer.imitate.algo.links

/**
 * @author rookie
 * @since 12-10-2019
 */


private val datas = arrayOf("a", "b", "c", "d", "e", "f")

fun main(args: Array<String>) {
    val nodes = SingleLinkedList<String>()
    for (data in datas) {
        nodes.insertToHead(data)
    }
    nodes.printAll()

    nodes.insertToHail("X")
    nodes.insertToHail("Y")
    nodes.insertToHail("Z")
    nodes.printAll()


    val node = nodes.findByIndex(4)
    node?.apply {
        println("find node at 4 :${data}")
        nodes.insertBefore(this,"dddd")
        println("insert dddd before :${data}")
        nodes.printAll()
    }

    val node1 = nodes.findByValue("d")
    node1.apply {
        println("find node data=4 ,it is $this")
        nodes.insertBefore(this, "lala")
        nodes.printAll()
    }

    nodes.deleteByValue("f")
    nodes.printAll()

    nodes.deleteByValue("Y")
    nodes.printAll()

    nodes.deleteByValue("Z")
    nodes.printAll()
}