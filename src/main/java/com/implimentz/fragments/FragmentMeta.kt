package com.implimentz.fragments

import android.support.annotation.StringRes
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.CLASS
import kotlin.annotation.AnnotationTarget.FILE

/**
 * Created by Alexander Efremenkov.
 * Date: 08.01.16, 12:10
 * In Intellij IDEA 15.0.1 Ultimate
 * email: implimentz@gmail.com
 * twitter: iamironz
 */
@Target(CLASS, FILE)
@Retention(RUNTIME)
annotation
/**
 * Annotation for annotating fragment class and getting from it fragment meta information
 */
class FragmentMeta(val isRoot: Boolean = false,
                   val toolbarShadow: Boolean = true,
                   val analyticHit: Boolean = true,
                   @StringRes val name: Int,
                   @StringRes val analytic: Int)
