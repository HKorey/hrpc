package com.hquery.hrpc.core.registry;

import com.hquery.hrpc.core.route.RouteClient;

import java.util.List;

/**
 * @author hquery.huang
 * 2019/4/3 20:18:35
 */
public interface DefaultRegistry {


    /**
     * 注册服务至注册中心
     *
     * @param clazz 服务类
     * @param address IP:PORT
     * @param weight 权重
     * @return void
     * @author hquery
     * 2019/4/3 20:26:45
     */
    void registerServer(Class<?> clazz, String address, String weight);

    /**
     * 注册客户端节点至注册中心
     *
     * @param clazz 服务类
     * @param address IP:PORT
     * @return void
     * @author hquery
     * 2019/4/3 20:26:45
     */
    void registerClient(Class<?> clazz, String address);

    /**
     * 获取远端服务
     *
     * @param clazz
     * @return java.util.List<com.hquery.hrpc.core.route.RouteClient>
     * @author hquery
     * 2019/5/17 17:24:16
     */
    List<RouteClient> getRemoteServers(Class<?> clazz);
}
