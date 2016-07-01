package com.implimentz.fragments

import android.content.res.Configuration
import android.support.annotation.CallSuper
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.implimentz.fragments.annotation.AnnotationManager

/**
 * Created by ironz.
 * Date: 19.01.16, 17:59
 * In Intellij IDEA 15.0.3 Ultimate
 * email: implimentz@gmail.com
 * twitter: iamironz
 */
open class Fragment<Data>(val arguments: Data? = null) {

    @Volatile internal var configurationChanged: Boolean = false
    @Volatile var finished: Boolean = false
        private set
    @Volatile var showing: Boolean = false
        private set
    @Volatile lateinit var name: String
        internal set
    @Volatile var view: View? = null
        private set

    @CallSuper
    open fun onConfigurationChanged(newConfig: Configuration) {

    }

    internal fun constructView(container: ViewGroup, inflater: LayoutInflater): View {
        tryRecreateView(container, inflater)
        updateStartFlags()
        return view as View
    }

    private fun tryRecreateView(container: ViewGroup, inflater: LayoutInflater) {
        if ((view == null) or configurationChanged) {
            view = inflateViewFromLayoutRes(inflater, container)
            onViewCreated(view as View, arguments)
        }
    }

    private fun inflateViewFromLayoutRes(inflater: LayoutInflater, container: ViewGroup): View {
        val meta = AnnotationManager.getLayoutMetaAnnotation(this)
        return inflater.inflate(meta.value, container, false)
    }

    @CallSuper
    open fun onViewCreated(view: View, arguments: Data?) {

    }

    @CallSuper
    open fun onResume() {
        showing = true
    }

    @CallSuper
    open fun onPause() {
        showing = false
    }

    @CallSuper
    open fun onDestroy() {
        updateStopFlags()
    }

    private fun updateStartFlags() {
        finished = false
        showing = true
        configurationChanged = false
    }

    private fun updateStopFlags() {
        finished = true
        showing = false
        configurationChanged = false
    }

    open fun hasNotEndedAction(): Boolean {
        return false
    }

    open fun onActionEndRequired() {

    }

    @Suppress("unused")
    @CallSuper
    open fun onOptionsItemSelected(item: MenuItem) {

    }

    open fun onBackPressed() {

    }
}
