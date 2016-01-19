package com.implimentz.fragments;

import android.support.annotation.StringRes;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by Alexander Efremenkov.
 * Date: 08.01.16, 12:10
 * In Intellij IDEA 15.0.1 Ultimate
 * email: implimentz@gmail.com
 * twitter: iamironz
 */
@Target(TYPE)
@Retention(RUNTIME)
/**
 * Annotation for annotating fragment class and getting from it fragment meta information
 */
public @interface FragmentMeta {
    boolean isRoot() default false;
    boolean toolbarShadow() default true;
    boolean analyticHit() default true;
    @StringRes int name();
    @StringRes int analytic();
}
