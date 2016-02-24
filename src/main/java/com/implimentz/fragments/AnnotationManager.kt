package com.implimentz.fragments

import android.support.v7.app.AppCompatActivity
import android.util.NoSuchPropertyException


/**
 * Created by ironz.
 * Date: 06.01.2016, 16:01.
 * In Intellij IDEA 15.0.3 Ultimate
 * email: implimentz@gmail.com
 * twitter: iamironz
 */
internal object AnnotationManager {

    fun <D, A : AppCompatActivity> getMeta(fragment: Fragment<D, A>): FragmentMeta {

        val clazz = FragmentMeta::class.java
        val simpleName: String = fragment.javaClass.simpleName

        val annotation: FragmentMeta? = fragment.javaClass.getAnnotation(clazz)

        return annotation ?: throw NoSuchPropertyException(
                "Fragment $simpleName must contain ${clazz.name} annotation"
        )
    }

    fun <D, A : AppCompatActivity> getLayoutId(fragment: Fragment<D, A>): Int {

        val clazz: Class<FragmentLayout> = FragmentLayout::class.java
        val simpleName: String = fragment.javaClass.simpleName

        val annotation: FragmentLayout? = fragment.javaClass.getAnnotation(clazz)

        if (annotation != null) {
            return annotation.value
        }

        throw NoSuchPropertyException("Fragment $simpleName must contain ${clazz.name} annotation")
    }

    fun <D, A : AppCompatActivity> getMenuId(fragment: Fragment<D, A>): Int {

        val clazz: Class<FragmentMenu> = FragmentMenu::class.java

        val annotation: FragmentMenu? = fragment.javaClass.getAnnotation(clazz)

        return if (annotation == null) 0 else annotation.value
    }
}
