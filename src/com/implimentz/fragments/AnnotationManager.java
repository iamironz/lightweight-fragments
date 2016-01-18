package info.wikiroutes.android.fragments;

import android.util.NoSuchPropertyException;


/**
 * Created by Alexander Efremenkov.
 * Date: 06.01.2016, 16:01.
 * In Intellij IDEA 14.1 Ultimate
 * aefremenkov@livamster.ru
 */
final class AnnotationManager {

    private static final String META_EXCEPTION = "Fragment %1$s must contain %2$s annotation";

    final FragmentMeta getFragmentMeta(final Fragment fragment) {
        final FragmentMeta annotation = fragment.getClass().getAnnotation(FragmentMeta.class);
        if(annotation == null) {
            throw new NoSuchPropertyException(
                    String.format(META_EXCEPTION, fragment.getClass().getSimpleName(), FragmentMeta.class.getSimpleName())
            );
        }
        return annotation;
    }
}
