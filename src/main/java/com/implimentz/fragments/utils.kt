package com.implimentz.fragments

import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup

/**
 * Created by Android Studio 1.5
 * Author: ironz
 * 05.02.16, 16:39
 * Email: implimentz@gmail.com
 */

fun ViewGroup.contains(view: View?): Boolean {

    if (childCount == 0) {
        return false
    }

    for (i in 0..childCount - 1) {
        if (getChildAt(i) === view) {
            return true
        }
    }

    return false
}

fun ViewGroup.hidePrevious() {

    if (childCount <= 1) {
        return
    }

    val previous = getChildAt(childCount - 2)
    previous.visibility = GONE
}

fun ViewGroup.showPrevious() {

    if (childCount <= 1) {
        return
    }

    val previous = getChildAt(childCount - 2)
    previous.visibility = VISIBLE
}