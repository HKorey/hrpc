package com.hquery.hrpc.core.proxy;

import com.hquery.hrpc.constants.GlobalConstants;
import com.hquery.hrpc.core.connector.RpcConnector;
import com.hquery.hrpc.core.discover.ServiceDiscovery;
import com.hquery.hrpc.core.model.RpcRequest;
import com.hquery.hrpc.core.model.RpcResponse;
import com.hquery.hrpc.core.route.AbstractRpcRoute;
import com.hquery.hrpc.core.route.RouteClient;
import com.hquery.hrpc.core.server.RemoteServerWrapper;
import com.hquery.hrpc.utils.SnowflakeIdWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hquery.huang
 * 2019/4/4 12:02:10
 */
@Slf4j
public class RpcClusterProxy implements RpcProxy {

    private SnowflakeIdWorker idWorker = new SnowflakeIdWorker(GlobalConstants.WORKER_ID, GlobalConstants.DATA_CENTER_ID);

    public RpcClusterProxy() {
    }

    @Override
    public Object getProxy(Class<?> clazz) {
        InvocationHandler invocationHandler = (proxy, method, args) -> {
            RpcRequest request = new RpcRequest();
            request.setRequestId(idWorker.nextId());
            request.setClassName(clazz.getName());
            request.setMethodName(method.getName());
            request.setParameterTypes(method.getParameterTypes());
            request.setParameters(args);
            log.info("request {} ", request);
            RemoteServerWrapper discover = ServiceDiscovery.discover(clazz);
            AbstractRpcRoute instance = AbstractRpcRoute.getInstance(discover.getRouteType());
            List<RouteClient> collect = discover.getRouteClients()
                    .stream()
                    .filter(RouteClient::isAlive)
                    .collect(Collectors.toList());
            if (CollectionUtils.isEmpty(collect)) {
                throw new RuntimeException("No Alive Server");
            }
            RouteClient routeClient = instance.doRoute(collect);
            RpcConnector connector = routeClient.getConnector();
            log.info("Invoke {} ", connector);
            RpcResponse response = connector.invoke(request);
            log.info("Resp {} ", response);
            if (response == null) {
                return null;
            }
            return response.getResult();
        };
        return Proxy.newProxyInstance(RpcCommonProxy.class.getClassLoader(), new Class[]{clazz}, invocationHandler);
    }

}
