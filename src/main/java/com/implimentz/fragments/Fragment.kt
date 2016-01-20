package com.implimentz.fragments

import android.content.res.Configuration
import android.support.annotation.CallSuper
import android.support.annotation.MenuRes
import android.support.annotation.StringRes
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.view.ActionMode
import android.support.v7.widget.Toolbar
import android.view.*
import android.widget.AdapterView
import android.widget.ImageView

/**
 * Created by Alexander Efremenkov.
 * Date: 19.01.16, 17:59
 * In Intellij IDEA 15.0.3 Ultimate
 * email: implimentz@gmail.com
 * twitter: iamironz
 */
@SuppressWarnings("unchecked")
open class Fragment<D, A : AppCompatActivity> {

    var isFinished = false
    var isShowing = false
    private var configurationChanged = false

    var view: View? = null
    var args: D? = null
    var activity: A? = null

    var title: String? = null
    var subTitle: String? = null

    var toolbar: Toolbar? = null
    private var actionMode: ActionMode? = null

    constructor() {

    }

    constructor(args: D) {
        this.args = args
    }

    protected fun clearViews() {
        if (view != null) {
            clearViews(view)
        }
    }

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

    fun setConfigurationChanged() {
        configurationChanged = true
    }

    open fun onConfigurationChanged(newConfig: Configuration) {

    }

    open fun onLowMemory() {

    }

    fun constructView(container: ViewGroup): View {
        if (view == null || configurationChanged) {
            view = onCreateView(activity?.layoutInflater as LayoutInflater, container, args as D)
        }

        configurationChanged = false
        isShowing = true
        return view as View
    }

    open fun onCreateView(inflater: LayoutInflater, container: ViewGroup, args: D): View {
        return View(activity)
    }

    @CallSuper
    open fun onResume() {
        isShowing = true
    }

    @CallSuper
    open fun onPause() {
        isShowing = false
    }

    @CallSuper
    open fun onDestroy() {
        isFinished = true
        isShowing = false
        configurationChanged = false
        clearViews()
        view = null
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

    fun startActionMode(callback: ActionMode.Callback): ActionMode {
        actionMode = activity?.startSupportActionMode(callback)
        return actionMode as ActionMode
    }

    fun finishActionMode() {
        actionMode?.finish()
    }

    open fun onActionModeEnabled(): Boolean {
        return false
    }

    @MenuRes
    open fun onCreateOptionMenu(menu: Menu): Int {
        return 0
    }

    @CallSuper
    open fun onOptionsItemSelected(item: MenuItem): Boolean {
        return false
    }

    val isHidden: Boolean get() = !isShowing

    fun getString(@StringRes stringId: Int): String {
        return activity?.getString(stringId) as String
    }

    fun getColor(colorId: Int): Int {
        return ContextCompat.getColor(activity, colorId)
    }

    fun onBackPressed() {
        activity?.onBackPressed()
    }
}
