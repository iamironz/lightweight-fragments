package com.implimentz.fragments;

import android.content.res.Configuration;
import android.support.annotation.CallSuper;
import android.support.annotation.MenuRes;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

/**
 * Created by Alexander Efremenkov.
 * Date: 05.01.16, 11:10
 * In Intellij IDEA 15.0.1 Ultimate
 * email: implimentz@gmail.com
 * twitter: iamironz
 */
@SuppressWarnings("unchecked")
public class Fragment<D, A extends AppCompatActivity> {

    private boolean isFinished = false;
    private boolean isShowing = false;
    private boolean configurationChanged = false;

    private View view;
    private D args;
    private A activity;

    private String title;
    private String subTitle;
    private ActionMode actionMode;
    private Toolbar toolbar;

    public Fragment() {

    }

    public Fragment(final D args) {
        this.args = args;
    }

    final void setActivity(final A owner) {
        this.activity = owner;
    }

    final void clearViews() {
        if (view != null) {
            clearViews(view);
        }
    }

    private void clearViews(final View v) {
        if (v == null) {
            return;
        }
        try {
            if (v.getBackground() != null) {
                v.getBackground().setCallback(null);
                return;
            }
            if (v instanceof ImageView) {
                ((ImageView) v).setImageBitmap(null);
                return;
            }
            if (v instanceof ViewGroup && !(v instanceof AdapterView)) {
                for (int i = 0; i < ((ViewGroup) v).getChildCount(); i++) {
                    clearViews(((ViewGroup) v).getChildAt(i));
                }
                ((ViewGroup) v).removeAllViews();
            }
        } catch (Exception ignored) {
        }
    }

    final void setConfigurationChanged() {
        configurationChanged = true;
    }

    public void onConfigurationChanged(final Configuration newConfig) {

    }

    public void onLowMemory() {

    }

    final View constructView(final ViewGroup container) {
        if (view == null || configurationChanged) {
            view = onCreateView(activity.getLayoutInflater(), container, args);
        }

        configurationChanged = false;
        isShowing = true;
        return view;
    }

    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final D args) {
        return new View(activity);
    }

    @CallSuper
    public void onResume() {
        isShowing = true;
    }

    @CallSuper
    public void onPause() {
        isShowing = false;
    }

    @CallSuper
    public void onDestroy() {
        isFinished = true;
        isShowing = false;
        configurationChanged = false;
        clearViews();
        view = null;
        args = null;
        activity = null;
        title = null;
        subTitle = null;
        actionMode = null;
        toolbar = null;
    }

    public boolean hasNotEndedAction() {
        return false;
    }

    public void onActionEndRequired() {

    }

    public final ActionMode startActionMode(final ActionMode.Callback callback) {
        actionMode = activity.startSupportActionMode(callback);
        return actionMode;
    }

    public final void finishActionMode() {
        if (actionMode == null) {
            return;
        }

        actionMode.finish();
    }

    public boolean onActionModeEnabled() {
        return false;
    }

    @MenuRes
    public int onCreateOptionMenu(final Menu menu) {
        return 0;
    }

    @CallSuper
    public boolean onOptionsItemSelected(final MenuItem item) {
        return false;
    }

    public final boolean isFinished() {
        return isFinished;
    }

    public final boolean isRunned() {
        return !isFinished;
    }

    public final boolean isShowing() {
        return isShowing;
    }

    public final boolean isHidden() {
        return !isShowing;
    }

    public final View getView() {
        return view;
    }

    public final D getArgs() {
        return args;
    }

    public final A getActivity() {
        return activity;
    }

    public final String getString(@StringRes final int stringId) {
        return getActivity().getString(stringId);
    }

    public final int getColor(final int colorId) {
        return ContextCompat.getColor(activity, colorId);
    }

    final String getTitle() {
        return title;
    }

    final String getSubTitle() {
        return subTitle;
    }

    final void setToolbar(final Toolbar toolbar) {
        this.toolbar = toolbar;
    }

    public final void onBackPressed() {
        activity.onBackPressed();
    }

    public final void setTitle(final String title) {
        if (isShowing && !isFinished) {
            toolbar.setTitle(title);
        }
        this.title = title;
    }

    public final void setSubTitle(final String subTitle) {
        if (isShowing && !isFinished) {
            toolbar.setSubtitle(subTitle);
        }
        this.subTitle = subTitle;
    }
}
