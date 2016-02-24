package com.implimentz.fragments

import android.support.v7.app.AppCompatActivity

/**
 * Created by ironz
 * Author: ironz
 * 09.01.16, 11:16
 * Email: implimentz@gmail.com
 */
interface StackChangeListener {
    fun <D, A: AppCompatActivity> onStackChanged(fragment: Fragment<D, A>, meta: FragmentMeta)
}
