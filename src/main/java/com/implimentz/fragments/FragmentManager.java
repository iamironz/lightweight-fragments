package com.implimentz.fragments;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexander Efremenkov.
 * Date: 04.01.16, 11:31
 * In Intellij IDEA 15.0.1 Ultimate
 * email: implimentz@gmail.com
 * twitter: iamironz
 */
@SuppressWarnings("unchecked")
public final class FragmentManager {

    private final AppCompatActivity activity;
    private final int containerId;
    private final ViewGroup container;
    private final AnnotationManager annotationManager;

    private Toolbar toolbar;
    private StackChangeListener listener;
    private final Handler handler = new Handler();

    public FragmentManager(final AppCompatActivity activity, final int containerId, final Toolbar toolbar) {
        this(activity, containerId);
        this.toolbar = toolbar;
    }

    public FragmentManager(final AppCompatActivity activity, final int containerId) {
        this.activity = activity;
        this.containerId = containerId;
        FragmentHolder.INSTANCE.register(containerId);
        container = (ViewGroup) activity.findViewById(containerId);
        this.annotationManager = new AnnotationManager();
    }

    public final void setStackChangeListener(final StackChangeListener listener) {
        this.listener = listener;
    }

    public final void destroyStack() {
        for (final FragmentData data : getStack()) {
            data.getFragment().onDestroy();
        }

        toolbar = null;
        listener = null;
        FragmentHolder.INSTANCE.unregister(containerId);
    }

    public final void onPause() {
        final List<FragmentData<Object, AppCompatActivity>> stack = getStack();

        if (stack.isEmpty()) {
            return;
        }

        final FragmentData data = stack.get(stack.size() - 1);
        final Fragment fragment = data.getFragment();

        if (!fragment.isShowing()) {
            return;
        }

        fragment.onPause();
    }

    public final void onResume() {
        final List<FragmentData<Object, AppCompatActivity>> stack = getStack();

        if (stack.isEmpty()) {
            return;
        }

        final FragmentData data = stack.get(stack.size() - 1);
        final Fragment fragment = data.getFragment();

        open(fragment);
    }

    public final List<FragmentData<Object, AppCompatActivity>> getStack() {
        return FragmentHolder.INSTANCE.getStackById(containerId);
    }

    public final void openFragment(final String name, final Fragment fragment) {
        openFragment0(name, fragment);
    }

    public final void openFragment(final String name, final Fragment fragment, final long delay) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                openFragment0(name, fragment);
            }
        }, delay);
    }

    public final void openFragment(final Fragment fragment) {
        openFragment0(fragment.getClass().getName(), fragment);
    }

    public final void openFragment(final Fragment fragment, final long delay) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                openFragment0(fragment.getClass().getName(), fragment);
            }
        }, delay);
    }

    private void openFragment0(final String name, final Fragment fragment) {
        final List<FragmentData<Object, AppCompatActivity>> stack = getStack();

        if (!stack.isEmpty()) {
            final FragmentData data = stack.get(stack.size() - 1);
            data.getFragment().onPause();
        }

        fragment.setActivity(activity);
        stack.add(new FragmentData(name, fragment));

        open(fragment);
    }

    public final boolean popFragment(final Fragment fragment) {
        return popFragment0(fragment.getClass().getName());
    }

    public final boolean popFragment(final Class<? extends Fragment> fragment) {
        return popFragment0(fragment.getName());
    }

    public final boolean popFragment(final String name) {
        return popFragment0(name);
    }

    private boolean popFragment0(final String name) {
        final List<FragmentData<Object, AppCompatActivity>> stack = getStack();

        if (stack.isEmpty()) {
            return false;
        }

        for (int i = 0; i < stack.size(); i++) {
            final FragmentData data = stack.get(i);
            if (data.getName().equals(name)) {
                closeFragmentRange(i + 1);
                open(data.getFragment());
                return true;
            }
        }

        return false;
    }

    private void closeFragmentRange(final int start) {
        final List<FragmentData<Object, AppCompatActivity>> stack = getStack();

        final List<FragmentData> forDeleting = new ArrayList<>();

        for (int i = start; i < stack.size(); i++) {
            final FragmentData data = stack.get(i);
            close(data.getFragment());
            forDeleting.add(data);
        }

        stack.removeAll(forDeleting);
    }

    public final void closeLastFragment() {
        final List<FragmentData<Object, AppCompatActivity>> stack = getStack();

        if (stack.isEmpty() || stack.size() < 2) {
            return;
        }

        final FragmentData old = stack.remove(stack.size() - 1);
        close(old.getFragment());
        getStack().remove(old);

        final FragmentData actual = stack.get(stack.size() - 1);
        open(actual.getFragment());
    }

    public final boolean hasNotEndedActions() {
        final List<FragmentData<Object, AppCompatActivity>> stack = getStack();

        if (stack.isEmpty()) {
            return false;
        }

        final Fragment fragment = stack.get(stack.size() - 1).getFragment();

        return fragment.hasNotEndedAction();
    }

    public final void onActionEndRequired() {
        final List<FragmentData<Object, AppCompatActivity>> stack = getStack();

        if (stack.isEmpty()) {
            return;
        }

        final Fragment fragment = stack.get(stack.size() - 1).getFragment();

        if (fragment.hasNotEndedAction()) {
            fragment.onActionEndRequired();
        }
    }

    public final void closeFragment(final Fragment name) {
        closeFragment0(name.getClass().getName());
    }

    public final void closeFragment(final Class<? extends Fragment> fragment) {
        closeFragment0(fragment.getName());
    }

    public final void closeFragment(final String name) {
        closeFragment0(name);
    }

    private void closeFragment0(final String name) {
        final List<FragmentData<Object, AppCompatActivity>> stack = getStack();

        if (stack.isEmpty()) {
            return;
        }

        for (FragmentData data : stack) {
            if (data.getName().equals(name)) {
                close(data.getFragment());
                getStack().remove(data);
                tryRestoreLast();
                return;
            }
        }
    }

    private void tryRestoreLast() {

        final List<FragmentData<Object, AppCompatActivity>> stack = getStack();

        if (stack.isEmpty()) {
            return;
        }

        final FragmentData data = stack.get(stack.size() - 1);
        open(data.getFragment());
    }

    private void open(final Fragment fragment) {

        if (fragment.isShowing()) {
            return;
        }

        final View view = fragment.constructView(container);

        setMenu(fragment);

        container.removeAllViews();
        container.addView(view);

        fragment.onResume();

        final FragmentMeta meta = annotationManager.getFragmentMeta(fragment);
        updateToolbar(fragment, meta);
        sendFragmentOpened(fragment, meta);
    }

    private void updateToolbar(final Fragment fragment, final FragmentMeta meta) {
        if(toolbar != null) {
            fragment.setToolbar(toolbar);
            final String title = fragment.getTitle();
            final String subTitle = fragment.getSubTitle();
            toolbar.setSubtitle(subTitle);
            if (title == null) {
                toolbar.setTitle(meta.name());
            } else {
                toolbar.setTitle(title);
            }
        }
    }

    private void sendFragmentOpened(final Fragment fragment, final FragmentMeta meta) {
        if (listener != null) listener.onStackChanged(fragment, meta);
    }

    public final boolean isActionModeEnabled() {
        final List<FragmentData<Object, AppCompatActivity>> stack = getStack();

        if (stack.isEmpty()) {
            return false;
        }

        final Fragment fragment = stack.get(stack.size() - 1).getFragment();

        return fragment.isShowing() && fragment.onActionModeEnabled();

    }

    public final void disableActionMode() {
        final List<FragmentData<Object, AppCompatActivity>> stack = getStack();

        if (stack.isEmpty()) {
            return;
        }

        final Fragment fragment = stack.get(stack.size() - 1).getFragment();

        if(!fragment.isShowing()) {
            return;
        }

        fragment.finishActionMode();
    }

    private void close(final Fragment fragment) {
        if (!fragment.isShowing()) {
            return;
        }

        container.removeAllViews();

        clearMenu();

        fragment.onDestroy();
    }

    private void setMenu(final Fragment fragment) {
        if (toolbar != null) {
            toolbar.getMenu().clear();
            final int resId = fragment.onCreateOptionMenu(toolbar.getMenu());
            if (resId != 0) {
                toolbar.inflateMenu(resId);
                toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        return fragment.onOptionsItemSelected(item);
                    }
                });
            }
        }
    }

    private void clearMenu() {
        if (toolbar != null) {
            toolbar.getMenu().clear();
        }
    }

    public final int getStackCount() {
        return getStack().size();
    }
}
