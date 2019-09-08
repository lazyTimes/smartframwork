package org.smart4j.framework.util;


import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;

import java.util.Collection;
import java.util.Map;

/**
 * 集合工具类
 */
public final class MyCollectionUtil {

    /**
     * 判断Collection 是否为空
     * @param collections 集合
     */
    public static boolean isEmpty(Collection<?> collections){
        return CollectionUtils.isEmpty(collections);
    }

    /**
     * 判断collection 不为空
     * @param collection 集合
     * @return
     */
    public static boolean isNotEmpty(Collection<?> collection){
        return !isEmpty(collection);
    }

    /**
     * 判断map是否为空
     * @param map map
     * @return
     */
    public static boolean isEmpty(Map<?,?> map){
        return MapUtils.isEmpty(map);
    }

    /**
     * 判断map是否不为空
     * @param map map
     * @return
     */
    public static boolean isNotEmpty(Map<?,?> map){
        return !isEmpty(map);
    }


}
