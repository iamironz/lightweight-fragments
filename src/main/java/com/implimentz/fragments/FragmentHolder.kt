@file:Suppress("unused")

package com.implimentz.fragments

import android.content.ComponentCallbacks
import android.content.Context
import android.content.res.Configuration
import java.io.Serializable
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList

/**
 * Created by ironz.
 * Date: 07.01.16, 11:40
 * In Intellij IDEA 15.0.1 Ultimate
 * email: implimentz@gmail.com
 * twitter: iamironz
 */
object FragmentHolder {

    private var registered: Boolean = false
    private val stack: MutableMap<Int, MutableList<Fragment<out Serializable>>> = ConcurrentHashMap()
    
    fun init(context: Context) {

        if (registered) {
            return
        }

        context.registerComponentCallbacks(callback)
        registered = true
    }

    private val callback = object : ComponentCallbacks {
        override fun onConfigurationChanged(newConfig: Configuration) {
            notifyConfigurationChanged(newConfig)
        }
        override fun onLowMemory() {
            //not needed
        }
    }

    private fun notifyConfigurationChanged(newConfig: Configuration) {
        stack.values.forEach {
            setForAllConfigChanges(it)
            callAllForConfigurationChanged(it, newConfig)
        }
    }

    private fun setForAllConfigChanges(it: MutableList<Fragment<out Serializable>>) {
        it.forEach {
            it.configurationChanged = true
        }
    }

    private fun callAllForConfigurationChanged(it: MutableList<Fragment<out Serializable>>, newConfig: Configuration) {
        it.filter {
            it.finished.not()
        }.forEach {
            it.onConfigurationChanged(newConfig)
            it.onPause()
        }
    }

    internal fun register(id: Int) {
        if (stack.containsKey(id)) {
            return
        }

        stack.put(id, ArrayList())
    }

    internal fun unregister(id: Int) {
        if (!stack.containsKey(id)) {
            return
        }

        stack.remove(id)
    }

    internal fun getStackById(id: Int): MutableList<Fragment<out Serializable>> {
        return stack[id] ?: CopyOnWriteArrayList()
    }
}
