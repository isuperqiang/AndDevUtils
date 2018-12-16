package com.richie.utils;

import java.util.Collection;

/**
 * @author Richie on 2017.12.08
 * 集合工具类
 */
public class CollectionUtils {

    private CollectionUtils() {
    }

    /**
     * 判断集合是否为空，不用 isEmpty(), 而用 size()>0
     *
     * @param collection
     * @param <E>
     * @return true 不为空，false 为空
     */
    public static <E> boolean notEmpty(Collection<E> collection) {
        return collection != null && collection.size() > 0;
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
        if (notEmpty(collection)) {
            try {
                collection.clear();
            } catch (Exception e) {
                // ignored
            }
        }
    }

}
