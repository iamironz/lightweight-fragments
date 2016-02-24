package com.implimentz.fragments

import android.support.annotation.MenuRes
import kotlin.annotation.AnnotationRetention.*
import kotlin.annotation.AnnotationTarget.*

/**
 * Created by Android Studio 1.5
 * Author: ironz
 * 24.02.16, 9:41
 * Email: implimentz@gmail.com
 */
@Target(CLASS, FILE)
@Retention(RUNTIME)
annotation
class FragmentMenu(@MenuRes val value: Int)