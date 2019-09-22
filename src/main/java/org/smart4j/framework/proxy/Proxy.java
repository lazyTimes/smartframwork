package org.smart4j.framework.proxy;

/**
 * @program: smartframwork
 * @description: 执行链式代理
 * @author: zhaoxudong
 * @create: 2019-09-21 21:07
 **/
public interface Proxy {

    Object doProxy(ProxyChain proxyChain) throws Throwable;
}
