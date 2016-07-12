package com.implimentz.fragments.annotation

import android.util.NoSuchPropertyException
import java.util.*


/**
 * Created by ironz.
 * Date: 06.01.2016, 16:01.
 * In Intellij IDEA 15.0.3 Ultimate
 * email: implimentz@gmail.com
 * twitter: iamironz
 */
internal object AnnotationManager {

    private val METAS_MAP: MutableMap<String, FragmentMeta> = HashMap()
    private val LAYOUTS_MAP: MutableMap<String, FragmentLayout> = HashMap()
    private val ANALYTICS_MAP: MutableMap<String, FragmentAnalytics> = HashMap()
    private val MENU_MAP: MutableMap<String, FragmentMenu> = HashMap()

    fun getMetaOrThrow(fragment: Any): FragmentMeta {
        return getMetaFromCacheOrRaw(fragment, METAS_MAP, true)!!
    }

    fun getLayoutOrThrow(fragment: Any): FragmentLayout {
        return getMetaFromCacheOrRaw(fragment, LAYOUTS_MAP, true)!!
    }

    fun getAnalyticsOrNull(fragment: Any): FragmentAnalytics? {
        return getMetaFromCacheOrRaw(fragment, ANALYTICS_MAP, false)
    }

    fun getMenuOrNull(fragment: Any): FragmentMenu? {
        return getMetaFromCacheOrRaw(fragment, MENU_MAP, false)
    }

    private inline fun <reified A : Annotation> getMetaFromCacheOrRaw(fragment: Any,
                                                                      map: MutableMap<String, A>,
                                                                      throwIfNotExists: Boolean): A? {

        val cachedName = fragment.javaClass.name

        if (map.containsKey(cachedName)) {
            return map[cachedName]!!
        }

        val clazz = A::class.java

        val annotation = fragment.javaClass.getAnnotation(clazz)

        if (annotation != null) {
            map.put(cachedName, annotation)
            return annotation
        }

        if (!throwIfNotExists) {
            return null
        }

        throw NoSuchPropertyException("Fragment ${fragment.javaClass.simpleName} must contain ${clazz.name} annotation")
    }
}
