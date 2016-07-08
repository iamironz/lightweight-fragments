@file:Suppress("unused")

package com.implimentz.fragments.annotation

import android.support.annotation.MenuRes
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.CLASS
import kotlin.annotation.AnnotationTarget.FILE

/**
 * Created by Alexander Efremenkov.
 * Date: 07.07.16, 16:21
 * In Intellij IDEA 2016.1.1 Ultimate
 * email: implimentz@gmail.com
 * twitter: iamironz
 */

@Target(CLASS, FILE)
@Retention(RUNTIME)
annotation
/**
 * Annotation for annotating fragment class and getting from it fragment meta information
 */
class FragmentMenu(val hasMenu: Boolean = false,
                   @MenuRes val menuId: Int)