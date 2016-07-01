package com.implimentz.fragments.annotation

import android.support.annotation.StringRes
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.CLASS
import kotlin.annotation.AnnotationTarget.FILE

/**
 * Created by ironz.
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
@Suppress("unused")
class FragmentMeta(@StringRes val name: Int,
                   val isRoot: Boolean = false,
                   val toolbarShadow: Boolean = true,
                   val analyticHit: Boolean = false,
                   @StringRes val analyticName: Int)
