package com.implimentz.fragments

import android.support.v7.app.AppCompatActivity
import android.util.NoSuchPropertyException


/**
 * Created by Alexander Efremenkov.
 * Date: 06.01.2016, 16:01.
 * In Intellij IDEA 14.1 Ultimate
 * aefremenkov@livamster.ru
 */
class AnnotationManager {
    fun getFragmentMeta(fragment: Fragment<Any, AppCompatActivity>): FragmentMeta {
        return fragment.javaClass.getAnnotation(FragmentMeta::class.java) ?: throw NoSuchPropertyException(
                "Fragment ${fragment.javaClass.simpleName} must contain ${FragmentMeta::class.simpleName} annotation")
    }
}
