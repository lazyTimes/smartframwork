package org.smart4j.framework.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * @program: smartframwork
 * @description: 切面代理
 * @author: zhaoxudong
 * @create: 2019-09-21 22:36
 **/
public abstract class AspectProxy implements Proxy {

    private static final Logger LOGGER = LoggerFactory.getLogger(AspectProxy.class);

    /**
     * 切面方法，使用责任链加上代理对象在切面进行代理操作
     * @param proxyChain
     * @return
     * @throws Throwable
     */
    public Object doProxy(ProxyChain proxyChain) throws Throwable {
        Object result = null;

        Class<?> targetClass = proxyChain.getTargetClass();
        Method targetMethod = proxyChain.getTargetMethod();
        Object[] methodParams = proxyChain.getMethodParams();

        begin();

        try{
            if(intercept(targetClass, targetMethod, methodParams)){
                before(targetClass, targetMethod, methodParams);
                result = proxyChain.doProxyChain();
                after(targetClass, targetMethod, methodParams);
            }else{
                result = proxyChain.doProxyChain();
            }
        }catch(Exception e){
            LOGGER.error("proxy failure", e);
            error(targetClass, targetMethod, methodParams, e);
        }finally {
            end();
        }
        return result;
    }

    protected void before(Class<?> targetClass, Method targetMethod, Object[] methodParams) throws Throwable{

    }


    /**
     * 开始代理操作之前的操作
     */
    public void begin() {

    }

    /**
     * 拦截参数，对于参数进行处理操作
     * 可以校验参数是否正确
     * @param cls
     * @param method
     * @param params
     * @return
     * @throws Throwable
     */
    public boolean intercept(Class<?> cls, Method method, Object[] params) throws Throwable {
        return true;
    }

    /**
     * 方法执行完成之后的
     * @param cls
     * @param method
     * @param params
     * @throws Throwable
     */
    public void after(Class<?> cls, Method method, Object[] params) throws Throwable{

    }

    /**
     * 对于错误的处理
     * @param cls
     * @param method
     * @param params
     * @throws Throwable
     */
    public void error(Class<?> cls, Method method, Object[] params, Throwable throwable) {

    }

    /**
     * 结束之后
     */
    public void end(){

    }
}
