package com.hquery.hrpc.core.proxy;

import com.hquery.hrpc.constants.GlobalConstants;
import com.hquery.hrpc.core.connector.RpcConnector;
import com.hquery.hrpc.core.model.RpcRequest;
import com.hquery.hrpc.core.model.RpcResponse;
import com.hquery.hrpc.utils.IdWorker;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by HQuery on 2018/12/1.
 */
public class RpcProxy {

    private RpcConnector rpcConnector;

    private IdWorker idWorker = new IdWorker(GlobalConstants.NODE_ID);

    public RpcProxy(RpcConnector rpcConnector) {
        this.rpcConnector = rpcConnector;
    }

    // TODO 转CGLIB
    public <T> T getProxy(final Class<T> clazz) {
        InvocationHandler handler = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                RpcRequest requst = new RpcRequest();
                requst.setRequestId(idWorker.nextId());
                requst.setClassName(clazz.getName());
                requst.setMethodName(method.getName());
                requst.setParameterTypes(method.getParameterTypes());
                requst.setParameters(args);
                RpcResponse response = rpcConnector.invoke(requst);
                if (response == null) return null;
                return response.getResult();
            }
        };
        return (T) Proxy.newProxyInstance(RpcProxy.class.getClassLoader(), new Class[]{clazz}, handler);
    }

//    public <T> T getProxy(final Class<T> clazz) {
//        InvocationHandler handler = new InvocationHandler() {
//            @Override
//            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//                RpcRequest requst = new RpcRequest();
//                requst.setRequestId(idWorker.nextId());
//                requst.setClassName(clazz.getName());
//                requst.setMethodName(method.getName());
//                requst.setParameterTypes(method.getParameterTypes());
//                requst.setParameters(args);
//                RpcResponse response = rpcConnector.invoke(requst);
//                if (response == null) return null;
//                return response.getResult();
//            }
//        };
//        return (T) Proxy.newProxyInstance(RpcProxy.class.getClassLoader(), new Class[]{clazz}, handler);
//    }

}
