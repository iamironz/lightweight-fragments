@file:Suppress("unused")

package com.implimentz.fragments.annotation

import android.support.annotation.LayoutRes
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.CLASS
import kotlin.annotation.AnnotationTarget.FILE


/**
 * Created by Android Studio 1.5
 * Author: ironz
 * 24.02.16, 9:08
 * Email: implimentz@gmail.com
 */
@Target(CLASS, FILE)
@Retention(RUNTIME)
annotation
class LayoutMeta(@LayoutRes val value: Int)