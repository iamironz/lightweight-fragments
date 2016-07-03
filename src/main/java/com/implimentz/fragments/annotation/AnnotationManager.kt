package com.implimentz.fragments.annotation

import android.util.NoSuchPropertyException
import com.implimentz.fragments.Fragment
import java.io.Serializable
import java.util.*


/**
 * Created by ironz.
 * Date: 06.01.2016, 16:01.
 * In Intellij IDEA 15.0.3 Ultimate
 * email: implimentz@gmail.com
 * twitter: iamironz
 */
internal object AnnotationManager {

    private val metasMap: MutableMap<String, FragmentMeta> = HashMap()
    private val layoutsMap: MutableMap<String, LayoutMeta> = HashMap()

    fun <D : Serializable> getFragmentMetaAnnotation(fragment: Fragment<D>): FragmentMeta {
        return getMetaFromCacheOrRaw(fragment, metasMap)
    }

    fun <D : Serializable> getLayoutMetaAnnotation(fragment: Fragment<D>): LayoutMeta {
        return getMetaFromCacheOrRaw(fragment, layoutsMap)
    }

    private inline fun <D : Serializable, reified A : Annotation> getMetaFromCacheOrRaw(fragment: Fragment<D>, map: MutableMap<String, A>): A {

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

        throw NoSuchPropertyException("Fragment ${fragment.javaClass.simpleName} must contain ${clazz.name} annotation")
    }
}
