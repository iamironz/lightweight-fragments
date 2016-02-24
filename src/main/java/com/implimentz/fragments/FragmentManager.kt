package com.implimentz.fragments

import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.ViewGroup
import java.util.*

/**
 * Created by ironz.
 * Date: 20.01.16, 9:32
 * In Intellij IDEA 15.0.3 Ultimate
 * email: implimentz@gmail.com
 * twitter: iamironz
 */
@SuppressWarnings("unchecked")
class FragmentManager(private val activity: AppCompatActivity,
                      private val containerId: Int,
                      private val toolbar: Toolbar) {

    private val handler: Handler = Handler()

    lateinit private var container: ViewGroup

    private var listener: StackChangeListener? = null

    init {
        FragmentHolder.register(containerId)
        container = activity.findViewById(containerId) as ViewGroup
    }

    fun setStackChangeListener(listener: StackChangeListener) {
        this.listener = listener
    }

    fun destroyStack() {
        getStack().forEach {
            it.fragment.onDestroy()
        }
        FragmentHolder.unregister(containerId)
    }

    fun onPause() {
        val stack = getStack()

        if (stack.isEmpty()) {
            return
        }

        val data = stack.last()
        val fragment = data.fragment

        if (fragment.showing.not()) {
            return
        }

        fragment.onPause()
    }

    fun onResume() {
        val stack = getStack()

        if (stack.isEmpty()) {
            return
        }

        val data = stack.last()
        val fragment = data.fragment

        open(fragment)
    }

    private fun getStack(): MutableList<FragmentData<out Any, out AppCompatActivity>> {
        return FragmentHolder.getStackById(containerId)
    }

    fun getFragments(): MutableList<Fragment<out Any, out AppCompatActivity>> {
        return FragmentHolder.getStackById(containerId)
                .map {
                    it.fragment
                }.toMutableList()
    }

    fun openFragment(name: String, fragment: Fragment<out Any, out AppCompatActivity>) {
        openFragment0(name, fragment)
    }

    fun openFragment(name: String, fragment: Fragment<out Any, out AppCompatActivity>, delay: Long) {
        handler.postDelayed({ openFragment0(name, fragment) }, delay)
    }

    fun openFragment(fragment: Fragment<out Any, out AppCompatActivity>) {
        openFragment0(fragment.javaClass.name, fragment)
    }

    fun openFragment(fragment: Fragment<out Any, out AppCompatActivity>, delay: Long) {
        handler.postDelayed({
            openFragment0(fragment.javaClass.name, fragment)
        }, delay)
    }

    private fun openFragment0(name: String, fragment: Fragment<out Any, out AppCompatActivity>) {
        val stack = getStack()

        if (stack.isEmpty().not()) {
            val data = stack.last()
            disableActionMode()
            data.fragment.onPause()
        }

        fragment.setOwner(activity)

        stack.add(FragmentData(name, fragment))

        open(fragment)
    }

    fun popFragment(fragment: Fragment<out Any, out AppCompatActivity>): Boolean {
        return popFragment0(fragment.javaClass.name)
    }

    fun popFragment(fragment: Class<out Fragment<out Any, out AppCompatActivity>>): Boolean {
        return popFragment0(fragment.name)
    }

    fun popFragment(name: String): Boolean {
        return popFragment0(name)
    }

    private fun popFragment0(name: String): Boolean {
        val stack = getStack()

        if (stack.isEmpty()) {
            return false
        }

        for (i in stack.indices) {
            val data = stack[i]
            if (data.name == name) {
                closeFragmentRange(i.inc())
                open(data.fragment)
                return true
            }
        }

        return false
    }

    private fun closeFragmentRange(start: Int) {
        val stack = getStack()

        val forDeleting = ArrayList<FragmentData<out Any, out AppCompatActivity>>()

        for (i in start..stack.lastIndex) {
            val data = stack[i]
            close(data.fragment)
            forDeleting.add(data)
        }

        stack.removeAll(forDeleting)
    }

    fun closeLastFragment() {
        val stack = getStack()

        if (stack.isEmpty() || stack.size < 2) {
            return
        }

        val old = stack.removeAt(stack.lastIndex)
        close(old.fragment)
        getStack().remove(old)

        val actual = stack.last()
        open(actual.fragment)
    }

    fun onBackPressed() {
        val stack = getStack()

        if (stack.isEmpty() || stack.size < 2) {
            return
        }

        val item = stack.last()
        item.fragment.onBackPressed()
    }

    fun hasNotEndedActions(): Boolean {
        val stack = getStack()

        if (stack.isEmpty()) {
            return false
        }

        val fragment = stack.last().fragment

        return fragment.hasNotEndedAction()
    }

    fun onActionEndRequired() {
        val stack = getStack()

        if (stack.isEmpty()) {
            return
        }

        val fragment = stack.last().fragment

        if (fragment.hasNotEndedAction()) {
            fragment.onActionEndRequired()
        }
    }

    fun closeFragment(name: Fragment<out Any, out AppCompatActivity>) {
        closeFragment0(name.javaClass.name)
    }

    fun closeFragment(fragment: Class<out Fragment<out Any, out AppCompatActivity>>) {
        closeFragment0(fragment.name)
    }

    fun closeFragment(name: String) {
        closeFragment0(name)
    }

    private fun closeFragment0(name: String) {
        val stack = getStack()

        if (stack.isEmpty()) {
            return
        }

        for (i in stack.lastIndex downTo 0) {
            val data = stack[i]
            if (data.name == name) {
                close(data.fragment)
                getStack().remove(data)
                tryRestoreLast()
                return
            }
        }
    }

    private fun tryRestoreLast() {

        val stack = getStack()

        if (stack.isEmpty()) {
            return
        }

        val data = stack.last()
        open(data.fragment)
    }

    private fun open(fragment: Fragment<out Any, out AppCompatActivity>) {

        if (fragment.showing) {
            return
        }

        fragment.setContainer(container)

        val view = fragment.constructView()

        if (container.contains(view).not()) {
            container.addView(view)
        }

        fragment.onResume()

        clearMenu()
        setMenu(fragment)

        val meta = AnnotationManager.getMeta(fragment)
        updateToolbar(fragment, meta)
        sendFragmentOpened(fragment, meta)
    }

    private fun updateToolbar(fragment: Fragment<out Any, out AppCompatActivity>, meta: FragmentMeta) {
        fragment.toolbar = toolbar
        val title = fragment.title
        val subTitle = fragment.subTitle
        toolbar.subtitle = subTitle
        if (title == null) {
            toolbar.setTitle(meta.name)
        } else {
            toolbar.title = title
        }
    }

    private fun sendFragmentOpened(fragment: Fragment<out Any, out AppCompatActivity>, meta: FragmentMeta) {
        listener?.let {
            it.onStackChanged(fragment, meta)
        }
    }

    fun isActionModeEnabled(): Boolean {
        val stack = getStack()

        if (stack.isEmpty()) {
            return false
        }

        val fragment = stack.last().fragment

        return fragment.showing && fragment.isActionModeEnabled()

    }

    fun disableActionMode() {
        val stack = getStack()

        if (stack.isEmpty()) {
            return
        }

        val fragment = stack.last().fragment

        if (fragment.showing.not()) {
            return
        }

        fragment.finishActionMode()
    }

    private fun close(fragment: Fragment<out Any, out AppCompatActivity>) {

        if (container.contains(fragment.view)) {
            container.removeView(fragment.view)
        }

        clearMenu()

        fragment.onDestroy()
    }

    private fun setMenu(fragment: Fragment<out Any, out AppCompatActivity>) {

        fragment.onCreateOptionMenu(toolbar)
        toolbar.setOnMenuItemClickListener {
            fragment.onOptionsItemSelected(it)
        }
    }

    private fun clearMenu() {
        toolbar.menu.clear()
        toolbar.setOnMenuItemClickListener(null)
    }

    fun getStackCount(): Int {
        return getStack().size
    }
}