package org.smart4j.framework.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @program: smartframwork
 * @description: 反射工具类
 * @author: zhaoxudong
 * @create: 2019-09-08 20:43
 **/
public final class RelfectionUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(RelfectionUtil.class);

    /**
     * 创建实例对象
     * @return
     */
    public static Object newInstance(Class<?> cls){
        Object instance = null;
        try {
            instance = cls.newInstance();
        } catch (Exception e) {
            LOGGER.error("new instance error", e);
            throw new RuntimeException(e);
        }
        return instance;
    }

    /**
     * 调用方法，使用反射调用
     * @param object 实例对象
     * @param method 方法体
     * @param params 参数
     * @return
     */
    public static Object invokeMethod(Object object, Method method, Object... params){

        Object result = null;

        // 一定要加上，强制访问
        try {
            method.setAccessible(true);
            method.invoke(object, params);
        } catch (Exception e){
            LOGGER.error("Method invoke error", e);
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * 设置成员变量的值
     * @param obj 实例对象
     * @param field 成员变量
     * @param value 值
     */
    public static void setField(Object obj, Field field, Object value){
        try {
            field.setAccessible(true);
            field.set(obj, value);
        } catch (IllegalAccessException e) {
            LOGGER.error("field set error", e);
            throw new RuntimeException(e);
        }
    }
}
