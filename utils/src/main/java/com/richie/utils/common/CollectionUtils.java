package com.richie.utils.common;

import java.util.Collection;

/**
 * @author Richie on 2017.12.08
 * 集合工具类
 */
public final class CollectionUtils {

    private CollectionUtils() {
    }

    /**
     * 判断集合是否为空
     *
     * @param collection
     * @param <E>
     * @return
     */
    public static <E> boolean isEmpty(Collection<E> collection) {
        return collection == null || collection.size() == 0;
    }

    public static <E> void clear(Collection<E> collection) {
        if (!isEmpty(collection)) {
            try {
                collection.clear();
            } catch (Exception e) {
                // ignored
            }
        }
    }

}
