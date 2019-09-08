package org.smart4j.framework.util;

import org.apache.commons.lang3.StringUtils;

/**
 * 字符串工具类
 */
public class MyStringUtil {

    public static boolean isEmpty(String str){
        return str != null && StringUtils.isEmpty(str.trim());
    }

    public static boolean isNotEmpty(String str){
        return !isEmpty(str);
    }
}
