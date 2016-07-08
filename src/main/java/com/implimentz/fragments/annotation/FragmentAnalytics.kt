@file:Suppress("unused")

package com.implimentz.fragments.annotation

import android.support.annotation.StringRes
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.CLASS
import kotlin.annotation.AnnotationTarget.FILE

/**
 * Created by Alexander Efremenkov.
 * Date: 07.07.16, 16:12
 * In Intellij IDEA 2016.1.1 Ultimate
 * email: implimentz@gmail.com
 * twitter: iamironz
 */

@Target(CLASS, FILE)
@Retention(RUNTIME)
annotation

/**
 * Annotation for annotating fragment class and getting from it analytic meta information
 */
class FragmentAnalytics(val analyticHit: Boolean = false,
                        @StringRes val analyticName: Int)