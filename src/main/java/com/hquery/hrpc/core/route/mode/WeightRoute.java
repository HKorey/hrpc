package com.hquery.hrpc.core.route.mode;

import com.hquery.hrpc.core.route.AbstractRpcRoute;
import com.hquery.hrpc.core.route.RouteClient;

import java.util.List;

/**
 * 带权重
 *
 * @author hquery.huang
 * 2019/4/4 14:02:34
 */
public class WeightRoute extends AbstractRpcRoute {

    public WeightRoute() {
    }

    @Override
    public RouteClient doRoute(List<RouteClient> routeClients) {
        return null;
    }
}
