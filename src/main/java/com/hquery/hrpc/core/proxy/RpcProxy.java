package com.hquery.hrpc.core.proxy;

/**
 * @author hquery.huang
 * 2019/5/21 12:23:07
 */
public interface RpcProxy {

    /**
     * 获取代理对象
     *
     * @param clazz
     * @return java.lang.Object
     * @author hquery
     * 2019/5/21 12:27:49
     */
    Object getProxy(Class<?> clazz);
}
