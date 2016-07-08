@file:Suppress("unused")

package com.implimentz.fragments

import android.content.res.Configuration
import android.support.annotation.CallSuper
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.implimentz.fragments.annotation.AnnotationManager
import java.io.Serializable

/**
 * Created by ironz.
 * Date: 19.01.16, 17:59
 * In Intellij IDEA 15.0.3 Ultimate
 * email: implimentz@gmail.com
 * twitter: iamironz
 */

/**
 * Base class for using as fragment container that represent activity lifecycle with specific lifecycle methods.
 *
 * @param arguments (optional) - for passing parametrized argument with some specific data needed by fragment.
 * Passing for [arguments] return parametrized argument and getting into [onViewCreated] method.
 * Must implement [Serializable] marker interface or if argument not passing set generic type as [NoArgs].
 * Please make sure to call super() constructor.
 */
open class Fragment<Data : Serializable>(val arguments: Data? = null) {

    @Volatile
    internal var configurationChanged: Boolean = false
    @Volatile
    var finished: Boolean = false
        private set
    @Volatile
    var showing: Boolean = false
        private set
    @Volatile
    var name: String? = null
        internal set
    @Volatile
    var view: View? = null
        private set

    /**
     * Function that calls by App [android.content.ComponentCallbacks] while screen orientation is changed
     *
     * @param newConfig - updated application config
     */
    @CallSuper
    open fun onConfigurationChanged(newConfig: Configuration) {

    }

    /**
     * Function that calls while fragment manager need to get new\created view
     *
     * @param container - target container for showing content in
     * @param inflater - activity inflater for inflate [com.implimentz.fragments.annotation.LayoutMeta]
     */
    internal fun constructView(container: ViewGroup, inflater: LayoutInflater): View {
        tryRecreateView(container, inflater)
        updateStartFlags()
        return view as View
    }

    /**
     * Function that creates view while view is null or device config has been changed
     *
     * @param container - target container for showing content in
     * @param inflater - activity inflater for inflate [com.implimentz.fragments.annotation.LayoutMeta]
     */
    private fun tryRecreateView(container: ViewGroup, inflater: LayoutInflater) {
        if ((view == null) or configurationChanged) {
            view = inflateViewFromLayoutRes(container, inflater)
            onViewCreated(view as View, arguments)
        }
    }

    /**
     * inflate layout from annotation or throws exception if annotation was not found
     * @see [AnnotationManager.getLayoutOrThrow]
     *
     * @param container - target container for showing content in
     * @param inflater - activity inflater for inflate [com.implimentz.fragments.annotation.LayoutMeta]
     */
    private fun inflateViewFromLayoutRes(container: ViewGroup, inflater: LayoutInflater): View {
        val meta = AnnotationManager.getLayoutOrThrow(this)
        return inflater.inflate(meta.value, container, false)
    }

    /**
     * Method that calls after @FragmentLayout layout created and added in container
     *
     * @param view - created view using for data binding
     * @param arguments - argument that was passed in fragment constructor
     */
    @CallSuper
    open fun onViewCreated(view: View, arguments: Data?) {

    }

    /**
     * Method that calls after container activity or fragment created/restored
     */
    @CallSuper
    open fun onResume() {
        showing = true
    }

    /**
     * Method that calls after container activity or fragment is minimized/overlapped
     */
    @CallSuper
    open fun onPause() {
        showing = false
    }

    /**
     * Method that calls after activity configuration changed/destroyed
     */
    @CallSuper
    open fun onDestroy() {
        updateStopFlags()
    }

    /**
     * Method that calls for update as active fragment status flags
     */
    private fun updateStartFlags() {
        finished = false
        showing = true
        configurationChanged = false
    }

    /**
     * Method that calls for update as inactive fragment status flags
     */
    private fun updateStopFlags() {
        finished = true
        showing = false
        configurationChanged = false
    }

    /**
     * Method that calls after FragmentManager onBackPressed() method called
     */
    open fun hasNotEndedAction(): Boolean {
        return false
    }

    /**
     * Method that calls after hasNotEndedAction() has return 'true'
     */
    open fun onActionEndRequired() {

    }

    /**
     * Method that calls after some menu item was clicked
     */
    @CallSuper
    open fun onOptionsItemSelected(item: MenuItem) {

    }

    /**
     * Method that calls after back button was pressed
     */
    open fun onBackPressed() {

    }
}
