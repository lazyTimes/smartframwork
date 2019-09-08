package org.smart4j.framework.util;

import org.apache.commons.lang3.ArrayUtils;

/**
 * @program: smartframwork
 * @description: 数组工具类
 * @author: zhaoxudong
 * @create: 2019-09-08 22:42
 **/
public final class MyArrayUtil {

    /**
     * 判断数组是为空的
     * @param arr 数组
     * @return
     */
    public static boolean isEmpty(Object[] arr){
        return ArrayUtils.isEmpty(arr);
    }

    /**
     * 判断数组是不为空的
     * @param arr 数组
     * @return
     */
    public static boolean isNotEmpty(Object[] arr){
        return !isEmpty(arr);
    }


}
