package org.smart4j.framework.helper;

import org.smart4j.framework.annotation.Aspect;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @program: smartframwork
 * @description: Aop工具类
 * @author: zhaoxudong
 * @create: 2019-09-22 23:22
 **/
public final class AopHelper {

    private static Set<Class<?>> createTargetClassSet(Aspect aspect) throws Throwable{
        Set<Class<?>> targetClassSet = new HashSet<Class<?>>();
        Class<? extends Annotation> annotation = aspect.value();
        if(annotation != null && !annotation.equals(Aspect.class)){
            targetClassSet.addAll(ClassHelper.getClassSetByAnnotation(annotation));
        }
        return targetClassSet;
    }

    private static Map<Class<?>, Set<Class>> createProxyMap() throws Exception{
        Map<Class<?>, Set<Class<?>>> proxyMap;
        // TODO 未完待续
        return null;
    }
}
