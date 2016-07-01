package com.implimentz.fragments.annotation

import android.util.NoSuchPropertyException
import com.implimentz.fragments.Fragment
import java.util.*


/**
 * Created by ironz.
 * Date: 06.01.2016, 16:01.
 * In Intellij IDEA 15.0.3 Ultimate
 * email: implimentz@gmail.com
 * twitter: iamironz
 */
internal object AnnotationManager {

    private val metasMap: MutableMap<Int, FragmentMeta> = HashMap()
    private val layoutsMap: MutableMap<Int, LayoutMeta> = HashMap()

    fun <D> getFragmentMetaAnnotation(fragment: Fragment<D>): FragmentMeta {
        return getMetaFromCacheOrRaw(fragment, metasMap)
    }

    fun <D> getLayoutMetaAnnotation(fragment: Fragment<D>): LayoutMeta {
        return getMetaFromCacheOrRaw(fragment, layoutsMap)
    }

    private inline fun <D, reified A : Annotation> getMetaFromCacheOrRaw(fragment: Fragment<D>, map: MutableMap<Int, A>): A {

        val hashCode = fragment.hashCode()

        if (map.containsKey(hashCode)) {
            return map[hashCode]!!
        }

        val clazz = A::class.java

        val annotation = fragment.javaClass.getAnnotation(clazz)

        if (annotation != null) {
            map.put(hashCode, annotation)
            return annotation
        }

        throw NoSuchPropertyException("Fragment ${fragment.javaClass.simpleName} must contain ${clazz.name} annotation")
    }
}
