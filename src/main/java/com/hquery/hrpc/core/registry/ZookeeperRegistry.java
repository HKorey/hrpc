package com.hquery.hrpc.core.registry;

import com.hquery.hrpc.core.connector.NettyRpcConnector;
import com.hquery.hrpc.core.discover.ServiceDiscovery;
import com.hquery.hrpc.core.route.RouteClient;
import com.hquery.hrpc.utils.SpringContextUtil;
import com.hquery.hrpc.zookeeper.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.CreateMode;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hquery.huang
 * 2019/4/3 20:18:56
 */
@Slf4j
@Component
@DependsOn("springContextUtil")
public class ZookeeperRegistry implements DefaultRegistry {

    private HrpcZkClient hrpcZkClient;

    @Override
    public void registerServer(Class<?> clazz, String address, String weight) {
        if (address == null || address.length() == 0) {
            throw new ZkException("Zookeeper服务端注册地址为空");
        }
        if (hrpcZkClient == null) {
            connectClient();
        }
        ZkClient client = hrpcZkClient.getClient();
        client.createIfNotExists(ZkConstants.ZK_REGISTRY_PATH, null, CreateMode.PERSISTENT);
        // 服务主节点（持久化）
        String path = ZkConstants.ZK_REGISTRY_PATH + "/" + clazz.getName();
        client.createIfNotExists(path, null, CreateMode.PERSISTENT);
        // 服务子节点 - 服务器列表（持久化）
        path += ZkConstants.ZK_SERVER_PATH;
        client.createIfNotExists(path, null, CreateMode.PERSISTENT);
        // 服务子节点 - 具体服务节点（临时节点）
        path += "/" + address + ":" + weight;
        client.createIfNotExists(path, null, CreateMode.EPHEMERAL);
    }

    @Override
    public void registerClient(Class<?> clazz, String address) {
        if (hrpcZkClient == null) {
            connectClient();
        }
        ZkClient client = hrpcZkClient.getClient();
        client.createIfNotExists(ZkConstants.ZK_REGISTRY_PATH, null, CreateMode.PERSISTENT);
        // 服务主节点（持久化）
        String path = ZkConstants.ZK_REGISTRY_PATH + "/" + clazz.getName();
        client.createIfNotExists(path, null, CreateMode.PERSISTENT);
        // 服务子节点 - 客户端列表（持久化）
        path += ZkConstants.ZK_CLIENT_PATH;
        client.createIfNotExists(path, null, CreateMode.PERSISTENT);
        // 服务子节点 - 具体客户端节点（临时节点）
        path += "/" + address;
        client.createIfNotExists(path, null, CreateMode.EPHEMERAL);
    }

    @Override
    public void refreshRemoteServers(Class<?> clazz) {
        if (hrpcZkClient == null) {
            connectClient();
        }
        ZkClient client = hrpcZkClient.getClient();
        String path = ZkConstants.ZK_REGISTRY_PATH + "/" + clazz.getName() + ZkConstants.ZK_SERVER_PATH;
        client.newChildWatcher(path, new ChildListener() {
            @Override
            protected void onAdd(String path, byte[] data) {
                try {
                    ServiceDiscovery.addServer(clazz, getRouteClient(path));
                } catch (Exception e) {
                    log.error("error", e);
                }
            }

            @Override
            protected void onDelete(String path) {
                try {
                    ServiceDiscovery.delServer(clazz, getRouteClient(path));
                } catch (Exception e) {
                    log.error("error", e);
                }
            }

            @Override
            protected void onUpdate(String path, byte[] newData) {
            }

            private RouteClient getRouteClient(String path) {
                String[] split = path.split(":");
                return new RouteClient()
                        .setConnector(new NettyRpcConnector(split[0].substring(split[0].lastIndexOf("/") + 1), Integer.parseInt(split[1]))).setWeight(Integer.parseInt(split[2]));
            }
        });

        if (ServiceDiscovery.getServer(clazz) == null) {
            throw new RuntimeException("获取到非法service : " + clazz.getName());
        }
        List<RouteClient> routeClients = client.gets(path)
                .stream()
                .map(p -> {
                    String[] split = p.split(":");
                    RouteClient routeClient = new RouteClient();
                    NettyRpcConnector nettyRpcConnector = new NettyRpcConnector(split[0].substring(split[0].lastIndexOf("/") + 1), Integer.parseInt(split[1]));
                    routeClient.setConnector(nettyRpcConnector).setWeight(Integer.parseInt(split[1]));
                    return routeClient;
                }).collect(Collectors.toList());
        ServiceDiscovery.resetRouteClients(clazz, routeClients);
    }

    /**
     * connectClient
     *
     * @param
     * @return void
     * @author hquery
     * 2019/4/3 21:43:06
     */
    private synchronized void connectClient() {
        if (hrpcZkClient != null) {
            return;
        }
        hrpcZkClient = SpringContextUtil.getBean(HrpcZkClient.class);
    }
}
