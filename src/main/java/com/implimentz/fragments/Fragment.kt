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

    private var _finished: Boolean = false
    val finished: Boolean
        get() = _finished

    private var _showing: Boolean = false
    val showing: Boolean
        get() = _showing

    private var configurationChanged: Boolean = false

    private lateinit var container: ViewGroup

    private var v: View? = null
    val view: View?
        get() = v

    private var _args: D? = null
    val args: D?
        get() = _args

    private var _owner: A? = null
    val owner: A?
        get() = _owner

    var title: String? = null

    var subTitle: String? = null

    var toolbar: Toolbar? = null
    private var actionMode: ActionMode? = null

    constructor() {
        this._args = null
    }

    constructor(args: D?) {
        this._args = args
    }

    protected fun clearViews() {
        if (v != null) {
            clearViews(v)
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun setOwner(a: AppCompatActivity) {
        this._owner = a as A
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

    fun getConstructView(): View {
        return constructView()
    }

    internal fun constructView(): View {
        if ((v == null) or configurationChanged) {
            v = onCreateView(_owner?.layoutInflater as LayoutInflater, container)
            onViewCreated(v as View, args)
        }

        configurationChanged = false
        _showing = true
        return v as View
    }

    private fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val layoutId: Int = AnnotationManager.getLayoutId(this)
        return inflater.inflate(layoutId, container, false)
    }

    open fun onViewCreated(view: View, arguments: D?) {

    }

    @CallSuper
    open fun onResume() {
        _showing = true
    }

    @CallSuper
    open fun onPause() {
        _showing = false
    }

    @CallSuper
    open fun onDestroy() {
        _finished = true
        _showing = false
        configurationChanged = false
        clearViews()
        v = null
        _args = null
        _owner = null
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
        actionMode = _owner?.startSupportActionMode(callback)
        return actionMode
    }

    fun finishActionMode() {
        actionMode?.finish()
    }

    open fun isActionModeEnabled(): Boolean {
        return false
    }

    internal fun onCreateOptionMenu(toolbar: Toolbar) {

        val menuId: Int = AnnotationManager.getMenuId(this)

        if (menuId != 0) {
            toolbar.inflateMenu(menuId)
            onMenuCreated(toolbar.menu)
        }
    }

    open fun onMenuCreated(menu: Menu) {

    }

    @CallSuper
    open fun onOptionsItemSelected(item: MenuItem): Boolean {
        return false
    }

    open fun onBackPressed() {

    }

    fun closeSelf() {
        _owner?.onBackPressed()
    }

    val isHidden: Boolean get() = !_showing

    fun getString(@StringRes stringId: Int): String? {
        return _owner?.getString(stringId)
    }

    fun getInt(@IntegerRes intId: Int): Int? {
        return _owner?.resources?.getInteger(intId)
    }

    fun getColor(@ColorRes colorId: Int): Int {
        return ContextCompat.getColor(_owner, colorId)
    }

    fun setContainer(container: ViewGroup) {
        this.container = container
    }
}
