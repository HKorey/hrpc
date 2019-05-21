package com.hquery.hrpc.core.proxy;

import com.hquery.hrpc.constants.GlobalConstants;
import com.hquery.hrpc.core.connector.RpcConnector;
import com.hquery.hrpc.core.discover.ServiceDiscovery;
import com.hquery.hrpc.core.model.RpcRequest;
import com.hquery.hrpc.core.route.AbstractRpcRoute;
import com.hquery.hrpc.core.route.RouteClient;
import com.hquery.hrpc.core.server.RemoteServerWrapper;
import com.hquery.hrpc.utils.SnowflakeIdWorker;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

/**
 * @author hquery.huang
 * 2019/4/4 12:02:10
 */
public class RpcClusterProxy implements RpcProxy {

    private SnowflakeIdWorker idWorker = new SnowflakeIdWorker(GlobalConstants.WORKER_ID, GlobalConstants.DATA_CENTER_ID);

    private AbstractRpcRoute route;

    public RpcClusterProxy() {
    }

    @Override
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
                RemoteServerWrapper discover = ServiceDiscovery.discover(clazz);
                AbstractRpcRoute instance = AbstractRpcRoute.getInstance(discover.getRouteType());
                RouteClient routeClient = instance.doRoute(discover.getRouteClients());
//                routeClient.getConnector().;


//                RpcResponse response = rpcConnector.invoke(request);
//                if (response == null) {
//                    return null;
//                }
//                return response.getResult();
                return null;
            }
        };
        return Proxy.newProxyInstance(RpcCommonProxy.class.getClassLoader(), new Class[]{clazz}, invocationHandler);
    }

}
