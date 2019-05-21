package com.hquery.hrpc.core.discover;

import com.hquery.hrpc.core.route.RouteClient;
import com.hquery.hrpc.core.server.RemoteServerWrapper;
import com.hquery.hrpc.utils.Threads;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.TimeUnit;


/**
 * 记录客户端用到的服务
 *
 * @author hquery.huang
 * 2019/4/4 14:34:42
 */
@Slf4j
public class ServiceDiscovery {

    private static final Map<Class<?>, RemoteServerWrapper> SERVICE_ROUTE_CACHE = new HashMap<>();

    public static boolean putService(Class<?> serviceInterface, RemoteServerWrapper wrapper) {
        return SERVICE_ROUTE_CACHE.putIfAbsent(serviceInterface, wrapper) == null;
    }

    public static RemoteServerWrapper getServer(Class<?> clazz) {
        return SERVICE_ROUTE_CACHE.get(clazz);
    }

    public static synchronized void resetRouteClients(Class<?> clazz, List<RouteClient> clients) {
        RemoteServerWrapper wrapper = SERVICE_ROUTE_CACHE.get(clazz);
        // 关闭 TCP链接
        List<RouteClient> routeClients = wrapper.getRouteClients();
        for (RouteClient routeClient : routeClients) {
            routeClient.getConnector();
        }
        wrapper.setRouteClients(clients);
    }

    public static synchronized void addServer(Class<?> clazz, RouteClient client) throws Exception {
        RemoteServerWrapper wrapper = SERVICE_ROUTE_CACHE.get(clazz);
        client.getConnector().start();
        wrapper.getRouteClients().add(client);
    }

    public static synchronized void delServer(Class<?> clazz, RouteClient client) throws Exception {
        RemoteServerWrapper wrapper = SERVICE_ROUTE_CACHE.get(clazz);
        List<RouteClient> routeClients = wrapper.getRouteClients();
        for (Iterator<RouteClient> item = routeClients.iterator(); item.hasNext(); ) {
            RouteClient rc = item.next();
            if (client.getConnector().getHost().equals(rc.getConnector().getHost())
                    && client.getConnector().getPort() == rc.getConnector().getPort()) {
//                item.remove();
                rc.setDown(true);
                rc.getConnector().stop();
            }
        }
    }

//    public static synchronized void updateServer(Class<?> clazz, RouteClient client) throws Exception {
//        RemoteServerWrapper wrapper = SERVICE_ROUTE_CACHE.get(clazz);
//        List<RouteClient> routeClients = wrapper.getRouteClients();
//        for (Iterator<RouteClient> item = routeClients.iterator(); item.hasNext(); ) {
//            RouteClient rc = item.next();
//            if (client.getConnector().getHost().equals(rc.getConnector().getHost())
//                    && client.getConnector().getPort() == rc.getConnector().getPort()) {
//                rc.getConnector().stop();
//                while (true) {
//                    if (rc.getConnector().isShutdown()) {
//                        break;
//                    }
//                    Threads.sleep(1000);
//                }
//                rc.setDown(true);
//                client.getConnector().start();
//                rc.setDown(false);
//            }
//        }
//    }

    public static Set<Class<?>> getAllServices() {
        return SERVICE_ROUTE_CACHE.keySet();
    }

    public static RemoteServerWrapper discover(Class<?> clazz) {
        RemoteServerWrapper wrapper = SERVICE_ROUTE_CACHE.get(clazz);
        if (wrapper == null) {
            log.error("未找到 {} 对应Server端服务", clazz.getName());
        }
        return wrapper;
    }

}
