package com.engineer.imitate.util

import android.content.Context
import android.widget.Toast

/**
 *
 * @author: Rookie
 * @date: 2018-08-21 09:54
 * @version V1.0
 */

fun Context.toastShort(message:String) {
    Toast.makeText(this,message, Toast.LENGTH_SHORT).show()
}


fun Context.toastLong(message: String){
    Toast.makeText(this,message, Toast.LENGTH_LONG).show()
}