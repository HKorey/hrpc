package com.hquery.hrpc.core.registry;

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
}
