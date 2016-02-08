package com.implimentz.fragments

import android.view.View
import android.view.ViewGroup

/**
 * Created by Android Studio 1.5
 * Author: ironz
 * 05.02.16, 16:39
 * Email: implimentz@gmail.com
 */

fun ViewGroup.contains(view: View?): Boolean {
    for (i in 0..childCount - 1) {
        if (getChildAt(i) === view) {
            return true
        }
    }
    return false
}
