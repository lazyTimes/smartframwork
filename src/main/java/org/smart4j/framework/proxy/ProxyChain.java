package org.smart4j.framework.proxy;

import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: smartframwork
 * @description: 代理链
 * @author: zhaoxudong
 * @create: 2019-09-21 21:08
 **/
public class ProxyChain {
    /**
     * 目标class类
     */
    private final Class<?> targetClass;
    /**
     * 目标实体对象
     */
    private final Object targerObject;
    /**
     * 目标class 方法
     */
    private final Method targetMethod;
    /**
     * 方法代理
     */
    private final MethodProxy methodProxy;
    /**
     * 方法参数
     */
    private final Object[] methodParams;


    /**
     * 代理列表
     */
    private List<Proxy> proxList = new ArrayList<Proxy>();
    /**
     * 代理索引
     */
    private int proxyIndex = 0;

    /**
     *
     * @param targetClass
     * @param targerObject
     * @param targetMethod
     * @param methodProxy
     * @param methodParams
     * @param proxList
     */
    public ProxyChain(Class<?> targetClass, Object targerObject, Method targetMethod, MethodProxy methodProxy, Object[] methodParams, List<Proxy> proxList) {
        this.targetClass = targetClass;
        this.targerObject = targerObject;
        this.targetMethod = targetMethod;
        this.methodProxy = methodProxy;
        this.methodParams = methodParams;
        this.proxList = proxList;
    }

    public Object[] getMethodParams() {
        return methodParams;
    }

    public Method getTargetMethod() {
        return targetMethod;
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }

    /**
     * 链式调用代理的方法
     * @return
     * @throws Throwable
     */
    public Object doProxyChain() throws Throwable{
        Object methodResult;
        if(proxyIndex < proxList.size()){
            methodResult = proxList.get(proxyIndex++).doProxy(this);
        } else{
            // 使用cglib 执行代理对象的方法
            methodResult = methodProxy.invokeSuper(targerObject, methodParams);
        }
        return methodResult;
    }

}
