package com.hquery.hrpc.core.proxy;

import com.hquery.hrpc.constants.GlobalConstants;
import com.hquery.hrpc.core.connector.RpcConnector;
import com.hquery.hrpc.core.model.RpcRequest;
import com.hquery.hrpc.core.model.RpcResponse;
import com.hquery.hrpc.utils.IdWorker;
import org.springframework.util.ClassUtils;

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

    public Object getProxy(Class<?> clazz) {
        InvocationHandler invocationHandler = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                RpcRequest request = new RpcRequest();
                request.setRequestId(idWorker.nextId());
                request.setClassName(clazz.getName());
                request.setMethodName(method.getName());
                request.setParameterTypes(method.getParameterTypes());
                request.setParameters(args);
                RpcResponse response = rpcConnector.invoke(request);
                if (response == null) {
                    return null;
                }
                return response.getResult();
            }
        };
        return Proxy.newProxyInstance(ClassUtils.getDefaultClassLoader(), new Class[]{clazz}, invocationHandler);
    }

}
