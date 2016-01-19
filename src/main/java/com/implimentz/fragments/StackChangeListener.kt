package com.implimentz.fragments

import android.support.v7.app.AppCompatActivity

/**
 * Created by Android Studio 1.5
 * Author: ironz
 * 09.01.16, 11:16
 * Email: implimentz@gmail.com
 */
interface StackChangeListener {
    fun onStackChanged(fragment: Fragment<Any, AppCompatActivity>, meta: FragmentMeta)
}
