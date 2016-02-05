package com.implimentz.fragments

import android.content.res.Configuration
import android.support.annotation.*
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.view.ActionMode
import android.support.v7.widget.Toolbar
import android.view.*
import android.widget.AdapterView
import android.widget.ImageView

/**
 * Created by ironz.
 * Date: 19.01.16, 17:59
 * In Intellij IDEA 15.0.3 Ultimate
 * email: implimentz@gmail.com
 * twitter: iamironz
 */
open class Fragment<D, A : AppCompatActivity> {

    private var finished: Boolean = false
    val isFinished: Boolean
        get() = finished

    private var showing: Boolean = false
    val isShowing: Boolean
        get() = showing

    private var configurationChanged: Boolean = false

    private var v: View? = null
    val view: View?
        get() = v

    private var args: D? = null
    val arguments: D?
        get() = args

    private var activity: A? = null
    val owner: A?
        get() = activity

    var title: String? = null

    var subTitle: String? = null

    var toolbar: Toolbar? = null
    private var actionMode: ActionMode? = null

    constructor() {
        this.args = null
    }

    constructor(args: D?) {
        this.args = args
    }

    protected fun clearViews() {
        if (v != null) {
            clearViews(v)
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun setOwner(a: AppCompatActivity) {
        this.activity = a as A
    }

    private fun clearViews(v: View?) {
        if (v == null) {
            return
        }
        try {
            if (v.background != null) {
                v.background.callback = null
                return
            }
            if (v is ImageView) {
                v.setImageBitmap(null)
                return
            }
            if (v is ViewGroup && v !is AdapterView<*>) {
                for (i in 0..v.childCount - 1) {
                    clearViews(v.getChildAt(i))
                }
                v.removeAllViews()
            }
        } catch (ignored: Exception) {
        }

    }

    internal fun setConfigurationChanged() {
        configurationChanged = true
    }

    open fun onConfigurationChanged(newConfig: Configuration) {

    }

    fun getConstructView(): View? {
        return v
    }

    fun constructView(container: ViewGroup): View {
        if ((v == null) or configurationChanged) {
            v = onCreateView(activity?.layoutInflater as LayoutInflater, container)
            onViewCreated(v as View)
        }

        configurationChanged = false
        showing = true
        return v as View
    }

    open fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return View(activity)
    }

    open fun onViewCreated(view: View) {

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
        finished = true
        showing = false
        configurationChanged = false
        clearViews()
        v = null
        args = null
        activity = null
        title = null
        subTitle = null
        actionMode = null
        toolbar = null
    }

    open fun hasNotEndedAction(): Boolean {
        return false
    }

    open fun onActionEndRequired() {

    }

    fun startActionMode(callback: ActionMode.Callback): ActionMode? {
        actionMode = activity?.startSupportActionMode(callback)
        return actionMode
    }

    fun finishActionMode() {
        actionMode?.finish()
    }

    open fun onActionModeEnabled(): Boolean {
        return false
    }

    @MenuRes
    open fun onCreateOptionMenu(menu: Menu): Int? {
        return null
    }

    @CallSuper
    open fun onOptionsItemSelected(item: MenuItem): Boolean {
        return false
    }

    open fun onBackPressed() {

    }

    fun closeSelf() {
        activity?.onBackPressed()
    }

    val isHidden: Boolean get() = !showing

    fun getString(@StringRes stringId: Int): String? {
        return activity?.getString(stringId)
    }

    fun getInt(@IntegerRes intId: Int): Int? {
        return activity?.resources?.getInteger(intId)
    }

    fun getColor(@ColorRes colorId: Int): Int {
        return ContextCompat.getColor(activity, colorId)
    }
}
