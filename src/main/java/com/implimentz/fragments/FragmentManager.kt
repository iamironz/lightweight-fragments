package com.implimentz.fragments

import android.os.Handler
import android.view.LayoutInflater
import android.view.ViewGroup
import com.implimentz.fragments.annotation.AnnotationManager
import java.util.*

/**
 * Created by ironz.
 * Date: 20.01.16, 9:32
 * In Intellij IDEA 15.0.3 Ultimate
 * email: implimentz@gmail.com
 * twitter: iamironz
 */

@Suppress("unused")
class FragmentManager(private val container: ViewGroup,
                      private val inflater: LayoutInflater,
                      private val listener: StackChangeListener) {

    private val handler: Handler = Handler()

    init {
        FragmentHolder.register(container.id)
    }

    @Suppress("unused")
    fun destroyStack() {
        getStack().forEach { it.onDestroy() }
        FragmentHolder.unregister(container.id)
    }

    @Suppress("unused")
    fun onPause() {

        val stack = getStack()

        if (stack.isEmpty()) {
            return
        }

        val fragment = stack.last()

        if (fragment.showing.not()) {
            return
        }

        fragment.onPause()
    }

    @Suppress("unused")
    fun onResume() {

        val stack = getStack()

        if (stack.isEmpty()) {
            return
        }

        val fragment = stack.last()

        tryShow(fragment)
    }

    private fun getStack(): MutableList<Fragment<out Any>> {
        return FragmentHolder.getStackById(container.id)
    }

    @Suppress("unused")
    fun getFragments(): MutableList<Fragment<out Any>> {
        return getStack()
    }

    @Suppress("unused")
    fun openFragment(name: String, fragment: Fragment<out Any>) {
        openFragment0(name, fragment)
    }

    @Suppress("unused")
    fun openFragment(name: String, fragment: Fragment<out Any>, delay: Long) {
        handler.postDelayed({ openFragment0(name, fragment) }, delay)
    }

    @Suppress("unused")
    fun openFragment(fragment: Fragment<out Any>) {
        openFragment0(fragment.javaClass.name, fragment)
    }

    @Suppress("unused")
    fun openFragment(fragment: Fragment<out Any>, delay: Long) {
        handler.postDelayed({ openFragment0(fragment.javaClass.name, fragment) }, delay)
    }

    private fun openFragment0(name: String, fragment: Fragment<out Any>) {

        val stack = getStack()

        if (stack.isEmpty().not()) {
            val fragment = stack.last()
            fragment.onPause()
        }

        fragment.name = name

        stack.add(fragment)

        tryShow(fragment)
    }

    @Suppress("unused")
    fun popFragment(fragment: Fragment<out Any>): Boolean {
        return popFragment0(fragment.javaClass.name)
    }

    @Suppress("unused")
    fun popFragment(fragment: Class<out Fragment<out Any>>): Boolean {
        return popFragment0(fragment.name)
    }

    @Suppress("unused")
    fun popFragment(name: String): Boolean {
        return popFragment0(name)
    }

    private fun popFragment0(name: String): Boolean {

        val stack = getStack()

        if (stack.isEmpty()) {
            return false
        }

        for (i in stack.indices) { //TODO implement foreach indexed with returning values
            val fragment = stack[i]
            if (fragment.name == name) {
                closeFragmentRange(i.inc())
                tryShow(fragment)
                return true
            }
        }

        return false
    }

    private fun closeFragmentRange(start: Int) {

        val stack = getStack()

        val forDeleting = ArrayList<Fragment<out Any>>()

        for (i in start..stack.lastIndex) {
            val fragment = stack[i]
            tryClose(fragment)
            forDeleting.add(fragment)
        }

        stack.removeAll(forDeleting)
    }

    @Suppress("unused")
    fun closeLastFragment() {

        val stack = getStack()

        if (stack.isEmpty() || stack.size < 2) {
            return
        }

        val old = stack.removeAt(stack.lastIndex)
        tryClose(old)
        getStack().remove(old)

        val actual = stack.last()
        tryShow(actual)
    }

    @Suppress("unused")
    fun onBackPressed() {

        val stack = getStack()

        if (stack.isEmpty() || stack.size < 2) {
            return
        }

        val item = stack.last()
        item.onBackPressed()
    }

    @Suppress("unused")
    fun hasNotEndedActions(): Boolean {

        val stack = getStack()

        if (stack.isEmpty()) {
            return false
        }

        val fragment = stack.last()

        return fragment.hasNotEndedAction()
    }

    @Suppress("unused")
    fun onActionEndRequired() {

        val stack = getStack()

        if (stack.isEmpty()) {
            return
        }

        val fragment = stack.last()

        if (fragment.hasNotEndedAction()) {
            fragment.onActionEndRequired()
        }
    }

    @Suppress("unused")
    fun closeFragment(name: Fragment<out Any>) {
        closeFragment0(name.javaClass.name)
    }

    @Suppress("unused")
    fun closeFragment(fragment: Class<out Fragment<out Any>>) {
        closeFragment0(fragment.name)
    }

    @Suppress("unused")
    fun closeFragment(name: String) {
        closeFragment0(name)
    }

    private fun closeFragment0(name: String) {

        val stack = getStack()

        if (stack.isEmpty()) {
            return
        }

        stack.asReversed()
                .filter {
                    it.name == name
                }
                .forEach {
                    tryClose(it)
                    getStack().remove(it)
                    tryRestoreLast()
                }
    }

    private fun tryRestoreLast() {

        val stack = getStack()

        if (stack.isEmpty()) {
            return
        }

        val fragment = stack.last()
        tryShow(fragment)
    }

    private fun tryShow(fragment: Fragment<out Any>) {

        if (fragment.showing) {
            return
        }

        tryAddViewToFront(fragment)
        callStackListener(fragment)
    }

    private fun tryAddViewToFront(fragment: Fragment<out Any>) {
        val view = fragment.constructView(container, inflater)
        if (container.contains(view)) {
            return
        }

        container.addView(view)
        container.hidePrevious()
        fragment.onResume()
    }

    private fun callStackListener(fragment: Fragment<out Any>) {
        val meta = AnnotationManager.getFragmentMetaAnnotation(fragment)
        listener.onStackChanged(fragment, meta)
    }

    private fun tryClose(fragment: Fragment<out Any>) {
        val view = fragment.view
        if (container.contains(view).not()) {
            return
        }

        container.showPrevious()
        container.removeView(view)
        fragment.onDestroy()
    }

    @Suppress("unused")
    fun getStackCount(): Int {
        return getStack().size
    }
}