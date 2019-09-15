package org.smart4j.framework.bean;

import java.lang.reflect.Method;

/**
 * @program: smartframwork
 * @description: 处理对象
 * @author: zhaoxudong
 * @create: 2019-09-09 22:16
 **/
public class Handler {

    /**
     * Controller 类
     */
    private Class<?> controllerClass;

    /**
     * Action 方法
     */
    private Method actionMethod;

    public Handler(Class<?> controllerClass, Method actionMethod) {
        this.controllerClass = controllerClass;
        this.actionMethod = actionMethod;
    }

    public Class<?> getControllerClass() {
        return controllerClass;
    }

    public Method getActionMethod() {
        return actionMethod;
    }
}
