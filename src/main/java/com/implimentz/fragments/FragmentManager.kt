@file:Suppress("unused")

package com.implimentz.fragments

import android.os.Handler
import android.view.LayoutInflater
import android.view.ViewGroup
import com.implimentz.fragments.annotation.AnnotationManager
import java.io.Serializable
import java.util.*

/**
 * Created by ironz.
 * Date: 20.01.16, 9:32
 * In Intellij IDEA 15.0.3 Ultimate
 * email: implimentz@gmail.com
 * twitter: iamironz
 */

/**
 * Class that represent stack and base stack control with holding in App class
 */
class FragmentManager(private val container: ViewGroup,
                      private val inflater: LayoutInflater,
                      private val listener: StackChangeListener) {

    private val handler: Handler = Handler()
    private val stack = FragmentHolder.getStackById(container.id)

    init {
        FragmentHolder.register(container.id)
    }

    fun destroyStack() {
        stack.forEach { it.onDestroy() }
        FragmentHolder.unregister(container.id)
    }

    fun onPause() {

        if (stack.isEmpty()) {
            return
        }

        val fragment = stack.last()

        if (fragment.showing.not()) {
            return
        }

        fragment.onPause()
    }

    fun onResume() {

        if (stack.isEmpty()) {
            return
        }

        val fragment = stack.last()

        tryShow(fragment)
    }

    fun getFragments(): MutableList<Fragment<out Serializable>> {
        return stack
    }

    fun openFragment(name: String, fragment: Fragment<out Serializable>) {
        openFragment0(name, fragment)
    }

    fun openFragment(name: String, fragment: Fragment<out Serializable>, delay: Long) {
        handler.postDelayed({ openFragment0(name, fragment) }, delay)
    }

    fun openFragment(fragment: Fragment<out Serializable>) {
        openFragment0(fragment.javaClass.name, fragment)
    }

    fun openFragment(fragment: Fragment<out Serializable>, delay: Long) {
        handler.postDelayed({ openFragment0(fragment.javaClass.name, fragment) }, delay)
    }

    private fun openFragment0(name: String, fragment: Fragment<out Serializable>) {

        if (stack.isEmpty().not()) {
            val last = stack.last()
            last.onPause()
        }

        fragment.name = name

        stack.add(fragment)

        tryShow(fragment)
    }

    fun popFragment(fragment: Fragment<out Serializable>): Boolean {
        return popFragment0(fragment.javaClass.name)
    }

    fun popFragment(fragment: Class<out Fragment<out Serializable>>): Boolean {
        return popFragment0(fragment.name)
    }

    fun popFragment(name: String): Boolean {
        return popFragment0(name)
    }

    private fun popFragment0(name: String): Boolean {

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

        val forDeleting = ArrayList<Fragment<out Serializable>>()

        for (i in start..stack.lastIndex) {
            val fragment = stack[i]
            tryClose(fragment)
            forDeleting.add(fragment)
        }

        stack.removeAll(forDeleting)
    }

    fun closeLastFragment() {

        if (stack.isEmpty() || stack.size < 2) {
            return
        }

        val old = stack[stack.lastIndex]
        tryClose(old)
        stack.remove(old)
        tryRestoreLast()
    }

    fun onBackPressed() {

        if (stack.isEmpty() || stack.size < 2) {
            return
        }

        val item = stack.last()
        item.onBackPressed()
    }

    fun hasNotEndedActions(): Boolean {

        if (stack.isEmpty()) {
            return false
        }

        val fragment = stack.last()

        return fragment.hasNotEndedAction()
    }

    fun onActionEndRequired() {

        if (stack.isEmpty()) {
            return
        }

        val fragment = stack.last()

        if (fragment.hasNotEndedAction()) {
            fragment.onActionEndRequired()
        }
    }

    fun closeFragment(name: Fragment<out Serializable>) {
        closeFragment0(name.javaClass.name)
    }

    fun closeFragment(fragment: Class<out Fragment<out Serializable>>) {
        closeFragment0(fragment.name)
    }

    fun closeFragment(name: String) {
        closeFragment0(name)
    }

    private fun closeFragment0(name: String) {

        if (stack.isEmpty()) {
            return
        }

        stack.asReversed()
                .filter {
                    it.name == name
                }
                .forEach {
                    tryClose(it)
                    stack.remove(it)
                    tryRestoreLast()
                }
    }

    private fun tryRestoreLast() {

        if (stack.isEmpty()) {
            return
        }

        val fragment = stack.last()
        tryShow(fragment)
    }

    private fun tryShow(fragment: Fragment<out Serializable>) {

        if (fragment.showing) {
            return
        }

        tryAddViewToFront(fragment)
        callStackListener(fragment)
    }

    private fun tryAddViewToFront(fragment: Fragment<out Serializable>) {
        val view = fragment.constructView(container, inflater)
        if (container.contains(view)) {
            return
        }

        container.addView(view)
        container.hidePrevious()
        fragment.onResume()
    }

    private fun callStackListener(fragment: Fragment<out Serializable>) {
        val meta = AnnotationManager.getFragmentMetaAnnotation(fragment)
        listener.onStackChanged(fragment, meta)
    }

    private fun tryClose(fragment: Fragment<out Serializable>) {
        val view = fragment.view
        if (container.contains(view).not()) {
            return
        }

        container.showPrevious()
        container.removeView(view)
        fragment.onDestroy()
    }

    fun getStackCount(): Int {
        return stack.size
    }

    fun stackIsEmpty(): Boolean {
        return stack.size < 2
    }
}