package org.smart4j.framework.helper;

import org.smart4j.framework.util.RelfectionUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @program: smartframwork
 * @description: 获取Bean的助手
 * @author: zhaoxudong
 * @create: 2019-09-08 21:05
 **/
public final class BeanHelper {

    /**
     * 定义bean映射
     * （存放bean类与bean 实例的映射关系）
     */
    private static Map<Class<?>, Object> BEAN_MAP = new HashMap<Class<?>, Object>();

    /**
     * 初始化
     * 1. 获取应用下所有的bean
     * 2. 使用反射生成实例
     * 3. 加入map
     */
    static {
        Set<Class<?>> beanCLassSet = ClassHelper.getBeanCLassSet();
        for (Class<?> cls : beanCLassSet) {
            Object obj = RelfectionUtil.newInstance(cls);
            BEAN_MAP.put(cls, obj);
        }
    }

    /**
     * 获取所有的映射内容
     * @return
     */
    public static Map<Class<?>, Object> getBeanMap() {
        return BEAN_MAP;
    }

    /**
     * 获取bean实例
     * @param cls 类名class
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(Class<T> cls){
        if(!BEAN_MAP.containsKey(cls)){
            throw new RuntimeException("can not get bean by class:\""+cls+"\"");
        }
        return (T) BEAN_MAP.get(cls);
    }


    public static void setBeanMap(Class<?> cls, Object object) {
        BEAN_MAP.put(cls, object);
    }


}
