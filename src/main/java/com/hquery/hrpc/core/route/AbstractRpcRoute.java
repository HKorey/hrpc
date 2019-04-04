package com.hquery.hrpc.core.route;


import com.hquery.hrpc.core.exception.RpcException;
import com.hquery.hrpc.core.route.mode.RandomRoute;
import com.hquery.hrpc.core.route.mode.RoundRobinRoute;
import com.hquery.hrpc.core.route.mode.WeightRoute;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hquery.huang
 * 2019/4/4 13:49:36
 */
public abstract class AbstractRpcRoute {

    public static final String ROUTE_TYPE_RANDOM = "random";

    public static final String ROUTE_TYPE_ROUND_ROBIN = "roundRobin";

    public static final String ROUTE_TYPE_WEIGHT = "weight";

    protected abstract RouteClient doRoute(List<RouteClient> routeClients);

    public static AbstractRpcRoute getInstance(String routeType) {
        if (ROUTE_TYPE_RANDOM.equals(routeType)) {
            return RandomHolder.INSTANCE;
        } else if (ROUTE_TYPE_ROUND_ROBIN.equals(routeType)) {
            return RoundRobinHolder.INSTANCE;
        } else if (ROUTE_TYPE_WEIGHT.equals(routeType)) {
            return WeightHolder.INSTANCE;
        }
        throw new RpcException("No available route type：" + routeType);
    }

    private static class RandomHolder {
        public static final RandomRoute INSTANCE = new RandomRoute();
    }

    private static class RoundRobinHolder {
        public static final RoundRobinRoute INSTANCE = new RoundRobinRoute();
    }

    private static class WeightHolder {
        public static final WeightRoute INSTANCE = new WeightRoute();
    }
}
