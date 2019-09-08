package org.smart4j.framework.helper;

import org.smart4j.framework.annotation.Inject;
import org.smart4j.framework.util.MyArrayUtil;
import org.smart4j.framework.util.MyCollectionUtil;
import org.smart4j.framework.util.RelfectionUtil;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * @program: smartframwork
 * @description: 依赖注入助手
 * @author: zhaoxudong
 * @create: 2019-09-08 22:38
 **/
public final class IocHelper {

    static {
        // 获取所有的bean 类与 bean实例之间的映射关系
        Map<Class<?>, Object> beanMap = BeanHelper.getBeanMap();
        if(MyCollectionUtil.isNotEmpty(beanMap)){
            // 遍历Beanmap
            for (Map.Entry<Class<?>, Object> classObjectEntry : beanMap.entrySet()) {
                // beanmap当中取出bean实例，bean类
                Class<?> beanClass = classObjectEntry.getKey();
                Object beanInstance = classObjectEntry.getValue();
                // 获取所有的成员变量
                Field[] beanFields = beanClass.getDeclaredFields();
                if(MyArrayUtil.isNotEmpty(beanFields)){
                    // 遍历成员变量
                    for (Field beanField : beanFields) {
                        // 判断当前是否存在@Inject 注解
                        if(beanField.isAnnotationPresent(Inject.class)){
                            // beanMap当中取出对应的实例
                            Class<?> beanFiledClass = beanField.getType();
                            Object beanFiledInstance = beanMap.get(beanFiledClass);
                            if(null != beanFiledInstance){
                                // 反射初始化BeanField的值
                                RelfectionUtil.setField(beanInstance, beanField, beanFiledInstance);
                            }
                        }
                    }
                }


            }
        }
    }
}
