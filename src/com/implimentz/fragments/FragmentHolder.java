package info.wikiroutes.android.fragments;

import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.res.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Alexander Efremenkov.
 * Date: 07.01.16, 11:40
 * In Intellij IDEA 15.0.1 Ultimate
 * email: implimentz@gmail.com
 * twitter: iamironz
 */
public final class FragmentHolder {

    private static boolean registered = false;
    private static final Map<Integer, List<FragmentData>> stack = new ConcurrentHashMap<>();

    public static void init(final Context context) {
        if(registered) {
            return;
        }

        context.registerComponentCallbacks(callback);
        registered = true;
    }

    private static final ComponentCallbacks callback = new ComponentCallbacks() {
        @Override
        public void onConfigurationChanged(Configuration newConfig) {
            notifyConfigurationChanged(newConfig);
        }
        @Override
        public void onLowMemory() {
            notifyLowMemory();
        }
    };

    private static void notifyConfigurationChanged(final Configuration newConfig) {
        for (final List<FragmentData> list : stack.values()) {
            for (final FragmentData data : list) {
                final Fragment fragment = data.getFragment();
                fragment.setConfigurationChanged();
                if (fragment.isShowing()) {
                    fragment.onPause();
                    fragment.onConfigurationChanged(newConfig);
                }
            }
        }
    }

    private static void notifyLowMemory() {
        for (final Map.Entry<Integer, List<FragmentData>> entry : stack.entrySet()) {
            for (final FragmentData data : entry.getValue()) {
                final Fragment fragment = data.getFragment();
                if (fragment.isShowing()) {
                    fragment.onLowMemory();
                }
            }
        }
    }

    static void register(final int id) {
        if (stack.containsKey(id)) {
            return;
        }

        stack.put(id, new CopyOnWriteArrayList<>());
    }

    static void unregister(final int id) {
        if (!stack.containsKey(id)) {
            return;
        }

        stack.remove(id);
    }

    static List<FragmentData> getStackById(final int id) {
        if(!stack.containsKey(id)) {
            return new ArrayList<>();
        }

        return stack.get(id);
    }
    
}
