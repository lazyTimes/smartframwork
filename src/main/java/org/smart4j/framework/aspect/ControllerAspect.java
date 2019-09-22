package org.smart4j.framework.aspect;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart4j.framework.annotation.Aspect;
import org.smart4j.framework.annotation.Controller;
import org.smart4j.framework.proxy.AspectProxy;

import java.lang.reflect.Method;

/**
 * @program: smartframwork
 * @description: 拦截所有的controller方法
 * @author: zhaoxudong
 * @create: 2019-09-22 22:46
 **/
@Aspect(Controller.class)
public class ControllerAspect extends AspectProxy {

    private static final Logger LOGGER = LoggerFactory.getLogger(ControllerAspect.class);

    private long begin;

    @Override
    protected void before(Class<?> targetClass, Method targetMethod, Object[] methodParams) throws Throwable {
        LOGGER.debug("---------begin---------");
        LOGGER.debug(String.format("class: %s", targetClass.getName()));
        LOGGER.debug(String.format("method: %s", targetMethod.getName()));
        begin = System.currentTimeMillis();
    }

    @Override
    public void after(Class<?> cls, Method method, Object[] params) throws Throwable {
        LOGGER.debug(String.format("time: %dms", System.currentTimeMillis() - begin));
        LOGGER.debug("---------end---------");
    }
}
