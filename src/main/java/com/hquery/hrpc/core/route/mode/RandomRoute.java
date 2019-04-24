package com.hquery.hrpc.core.route.mode;

import com.hquery.hrpc.core.exception.RpcException;
import com.hquery.hrpc.core.route.AbstractRpcRoute;
import com.hquery.hrpc.core.route.RouteClient;

import java.util.List;

/**
 * 随机
 *
 * @author hquery.huang
 * 2019/4/4 14:00:02
 */
public class RandomRoute extends AbstractRpcRoute {


    public RandomRoute() {
    }

    @Override
    public RouteClient doRoute(List<RouteClient> routeClients) {
        int size = routeClients.size();
        if (size == 0) {
            throw new RpcException("无可用服务");
        }


        return null;
    }
}
