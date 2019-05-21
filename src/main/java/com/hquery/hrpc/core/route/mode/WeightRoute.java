package com.hquery.hrpc.core.route.mode;

import com.hquery.hrpc.core.route.AbstractRpcRoute;
import com.hquery.hrpc.core.route.RouteClient;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 带权重
 *
 * @author hquery.huang
 * 2019/4/4 14:02:34
 */
@NoArgsConstructor
public class WeightRoute extends AbstractRpcRoute {

    @Override
    public RouteClient doRoute(List<RouteClient> routeClients) {
        return null;
    }
}
