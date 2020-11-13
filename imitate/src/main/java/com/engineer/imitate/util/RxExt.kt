package com.engineer.imitate.util

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

fun Disposable.add(cd: CompositeDisposable) {
    cd.add(this)
}