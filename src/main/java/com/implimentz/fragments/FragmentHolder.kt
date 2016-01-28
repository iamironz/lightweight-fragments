package com.implimentz.fragments

import android.content.ComponentCallbacks
import android.content.Context
import android.content.res.Configuration
import android.support.v7.app.AppCompatActivity
import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
 * Created by Alexander Efremenkov.
 * Date: 07.01.16, 11:40
 * In Intellij IDEA 15.0.1 Ultimate
 * email: implimentz@gmail.com
 * twitter: iamironz
 */
public object FragmentHolder {

    private var registered: Boolean = false
    private val stack: MutableMap<Int, MutableList<FragmentData<out Any, out AppCompatActivity>>> = ConcurrentHashMap()

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
            notifyLowMemory()
        }
    }

    private fun notifyConfigurationChanged(newConfig: Configuration) {
        stack.values.forEach {
            it.forEach {
                it.fragment.setConfigurationChanged()
            }
            it.filter {
                it.fragment.isFinished.not()
            }.filter {
                it.fragment.isShowing
            }.forEach {
                it.fragment.onConfigurationChanged(newConfig)
                it.fragment.onPause()
            }
        }
    }

    private fun notifyLowMemory() {
        stack.values.forEach {
            it.filter {
                it.fragment.isFinished.not()
            }.filter {
                it.fragment.isShowing
            }.forEach {
                it.fragment.onLowMemory()
            }
        }
    }

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    fun register(id: Int) {
        if (stack.containsKey(id)) {
            return
        }

        stack.put(id, ArrayList())
    }

    fun unregister(id: Int) {
        if (!stack.containsKey(id)) {
            return
        }

        stack.remove(id)
    }

    fun getStackById(id: Int): MutableList<FragmentData<out Any, out AppCompatActivity>> {
        if (!stack.containsKey(id)) {
            return ArrayList()
        }

        return stack[id]!!
    }

}
