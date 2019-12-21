package com.engineer.plugin.extensions.model

/**
 * @author rookie
 * @since 11-29-2019
 */

class TaskDetail : Comparable<TaskDetail> {

    var total = 0f
    var start = 0L
    var end = 0L
    var name = ""

    override fun compareTo(other: TaskDetail): Int {
        return this.total.compareTo(other.total)
    }

}