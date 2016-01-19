package com.implimentz.fragments

import android.support.v7.app.AppCompatActivity

/**
 * Created by Alexander Efremenkov.
 * Date: 19.01.16, 14:57
 * In Intellij IDEA 15.0.1 Ultimate
 * email: implimentz@gmail.com
 * twitter: iamironz
 */
internal data class FragmentData<T, D: AppCompatActivity>(public val name: String, public val fragment: Fragment<T, D>)