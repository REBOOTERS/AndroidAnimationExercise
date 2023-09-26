package com.bird.internal

import android.content.res.Resources


internal val Number.dp get() = (toInt() * Resources.getSystem().displayMetrics.density).toInt()

internal val screenWidth = Resources.getSystem().displayMetrics.widthPixels