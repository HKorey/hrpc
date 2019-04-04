package com.hquery.hrpc.core.discover;

import com.hquery.hrpc.core.route.RouteClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 记录客户端用到的服务
 *
 * @author hquery.huang
 * 2019/4/4 14:34:42
 */
public class ServiceDiscovery {

    private static final Map<Class<?>, List<RouteClient>> SERVICE_ROUTE_CACHE = new HashMap<>();

    public static boolean putService(Class<?> clazz, List<RouteClient> services) {
        return SERVICE_ROUTE_CACHE.putIfAbsent(clazz, services) == null;
    }

    public static List<RouteClient> getServices(Class<?> clazz) {
        return SERVICE_ROUTE_CACHE.get(clazz);
    }

}
