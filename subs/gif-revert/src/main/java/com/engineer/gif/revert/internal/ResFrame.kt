package com.engineer.gif.revert.internal

/**
 * @author rookie
 * @since 07-06-2019
 */
internal data class ResFrame(var delay: Int, var path: String) : Comparable<String> {
    override fun compareTo(other: String): Int {
        return other.compareTo(path)
    }
}