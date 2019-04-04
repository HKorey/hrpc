package com.hquery.hrpc.core.proxy;

import com.hquery.hrpc.constants.GlobalConstants;
import com.hquery.hrpc.core.model.RpcRequest;
import com.hquery.hrpc.core.model.RpcResponse;
import com.hquery.hrpc.core.route.AbstractRpcRoute;
import com.hquery.hrpc.utils.SnowflakeIdWorker;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author hquery.huang
 * 2019/4/4 12:02:10
 */
public class RpcClusterProxy {

    private AbstractRpcRoute route;

    private SnowflakeIdWorker idWorker = new SnowflakeIdWorker(GlobalConstants.WORKER_ID, GlobalConstants.DATA_CENTER_ID);

    public RpcClusterProxy(AbstractRpcRoute route) {
        this.route = route;
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
//                RpcResponse response = rpcConnector.invoke(request);
//                if (response == null) {
//                    return null;
//                }
//                return response.getResult();
                return null;
            }
        };
        return Proxy.newProxyInstance(RpcProxy.class.getClassLoader(), new Class[]{clazz}, invocationHandler);
    }

}
