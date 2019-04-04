package com.hquery.hrpc.core.route.mode;

import com.hquery.hrpc.core.route.AbstractRpcRoute;
import com.hquery.hrpc.core.route.RouteClient;

import java.util.List;

/**
 * 轮询
 *
 * @author hquery.huang
 * 2019/4/4 14:01:32
 */
public class RoundRobinRoute extends AbstractRpcRoute {


    public RoundRobinRoute() {
    }

    @Override
    public RouteClient doRoute(List<RouteClient> routeClients) {
        return null;
    }
}
