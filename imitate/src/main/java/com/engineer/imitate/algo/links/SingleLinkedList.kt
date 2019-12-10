package com.engineer.imitate.algo.links

/**
 * @author rookie
 * @since 12-10-2019
 */

class SingleLinkedList<T> {

    private var head: ZNode<T>? = null

    fun printAll() {

        var temp = head
        while (temp != null) {
            print(temp.data.toString() + " ")
            temp = temp.next
        }
        println("\n--------------------")
    }

    fun deleteByValue(value: T) {
        if (head == null) {
            return
        }
        var target = head
        var temp: ZNode<T>? = null
        while (target != null && target.data != value) {
            temp = target
            target = target.next
        }
        if (target == null) {
            return
        }
        if (temp == null) {
            head = head?.next
        } else {
            temp.next = temp.next?.next
        }
    }

    fun findByValue(value: T): ZNode<T>? {
        var temp = head
        while (temp != null && temp.data != value) {
            temp = temp.next
        }
        return temp
    }

    fun findByIndex(position: Int): ZNode<T>? {
        var temp = head
        var pos = 0
        while (temp != null && pos != position) {
            temp = temp.next
            ++pos
        }
        return temp
    }

    fun insertBefore(target: ZNode<T>?, value: T) {
        val newNode = ZNode(value, null)
        insertBefore(target, newNode)
    }

    private fun insertBefore(target: ZNode<T>?, newNode: ZNode<T>?) {
        if (newNode == null) {
            return
        }
        if (target == head) {
            insertToHead(newNode)
            return
        }
        var temp = head
        while (temp != null && temp.next != target) {
            temp = temp.next
        }
        if (temp == null) {
            return
        }
        newNode.next = target
        temp.next = newNode
    }


    fun insertToHail(value: T) {
        val newNode = ZNode(value, null)

        if (head == null) {
            head = newNode
        } else {
            var temp = head
            while (temp?.next != null) {
                temp = temp.next
            }
            newNode.next = temp?.next
            temp?.next = newNode
        }
    }


    fun insertToHead(value: T) {
        val newNode = ZNode(value, null)
        insertToHead(newNode)
    }

    fun insertToHead(newNode: ZNode<T>) {
        if (head == null) {
            head = newNode
        } else {
            newNode.next = head
            head = newNode
        }
    }
}