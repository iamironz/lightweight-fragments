package com.implimentz.fragments;

/**
 * Created by Alexander Efremenkov.
 * Date: 07.01.16, 11:38
 * In Intellij IDEA 15.0.1 Ultimate
 * email: implimentz@gmail.com
 * twitter: iamironz
 */
final class FragmentData {

    private final String name;
    private final Fragment fragment;

    FragmentData(final String name, final Fragment fragment) {
        this.name = name;
        this.fragment = fragment;
    }

    final String getName() {
        return name;
    }
    final Fragment getFragment() {
        return fragment;
    }
}
