package com.engineer.plugin.extensions.model

import org.jetbrains.annotations.NotNull

class TaskDetail implements Comparable<Object> {
    long total
    long start
    long end
    String name

    @Override
    int compareTo(@NotNull Object o) {
        TaskDetail d = o
        return this.total - d.total
    }
}