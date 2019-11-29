package com.engineer.plugin.extensions.model

/**
 * @author rookie
 * @since 11-29-2019
 */

class TaskDetail : Comparable<Any> {

    var total = 0L
    var start = 0L
    var end = 0L
    var name = ""

    override fun compareTo(other: Any): Int {
        return if (other is TaskDetail) {
            (this.total - other.total).toInt()
        } else {
            0
        }
    }

}