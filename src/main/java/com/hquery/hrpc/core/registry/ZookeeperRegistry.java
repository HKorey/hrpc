package com.hquery.hrpc.core.registry;

import com.hquery.hrpc.utils.SpringContextUtil;
import com.hquery.hrpc.zookeeper.HrpcZkClient;
import com.hquery.hrpc.zookeeper.ZkClient;
import com.hquery.hrpc.zookeeper.ZkConstants;
import com.hquery.hrpc.zookeeper.ZkException;
import org.apache.zookeeper.CreateMode;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author hquery.huang
 * 2019/4/3 20:18:56
 */
@Component
@DependsOn("springContextUtil")
public class ZookeeperRegistry implements DefaultRegistry {

    private volatile HrpcZkClient hrpcZkClient;

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
        path += "/" + ZkConstants.ZK_SERVER_PATH;
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
        path += "/" + ZkConstants.ZK_CLIENT_PATH;
        client.createIfNotExists(path, null, CreateMode.PERSISTENT);
        // 服务子节点 - 具体客户端节点（临时节点）
        path += "/" + address;
        client.createIfNotExists(path, null, CreateMode.EPHEMERAL);
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
