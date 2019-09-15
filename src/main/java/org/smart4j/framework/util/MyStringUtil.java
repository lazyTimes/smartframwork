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

    /**
     * 切割字符串辅助方法
     * @param body
     * @param s
     * @return
     */
    public static String[] spliteString(String body, String s) {
        return StringUtils.split(body, s);
    }
}
