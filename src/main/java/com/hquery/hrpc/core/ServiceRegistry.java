package com.hquery.hrpc.core;

import com.hquery.hrpc.constants.GlobalConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @author hquery.huang
 * 2019/3/23 18:37:09
 */
@Slf4j
public class ServiceRegistry {

    private CountDownLatch latch = new CountDownLatch(1);

    public void registerClient(Class<?> clazz, String node) {
        if (node != null) {
            ZooKeeper zk = connectServer();
            if (zk != null) {
                createClient(zk, clazz, node);
            }
        }
    }

    public void registerServer(Class<?> clazz, String node, String data) {
        if (node != null) {
            ZooKeeper zk = connectServer();
            if (zk != null) {
                createServer(zk, clazz, node, data);
            }
        }
    }

    private ZooKeeper connectServer() {
        ZooKeeper zk = null;
        try {
            zk = new ZooKeeper(GlobalConstants.ZK_ADDRESS, GlobalConstants.ZK_SESSION_TIMEOUT, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    if (event.getState() == Event.KeeperState.SyncConnected) {
                        latch.countDown();
                    }
                }
            });
            latch.await();
        } catch (IOException | InterruptedException e) {
            log.error("error", e);
        }
        return zk;
    }

    private void createServer(ZooKeeper zk, Class<?> clazz, String node, String data) {
        try {
            createNode(zk, GlobalConstants.ZK_REGISTRY_PATH, "", CreateMode.PERSISTENT);
            String path = GlobalConstants.ZK_REGISTRY_PATH + "/" + clazz.getName();
            createNode(zk, path, "", CreateMode.PERSISTENT);
            path = path + GlobalConstants.ZK_SERVER_PATH;
            createNode(zk, path, "", CreateMode.PERSISTENT);
            path = path + "/" + node + ":" + data;
            createNode(zk, path, "", CreateMode.EPHEMERAL);
        } catch (Exception e) {
            log.error("error", e);
        }
    }

    private void createClient(ZooKeeper zk, Class<?> clazz, String node) {
        try {

            createNode(zk, GlobalConstants.ZK_REGISTRY_PATH, "", CreateMode.PERSISTENT);
            String path = GlobalConstants.ZK_REGISTRY_PATH + "/" + clazz.getName();
            createNode(zk, path, "", CreateMode.PERSISTENT);
            path = path + GlobalConstants.ZK_CLIENT_PATH;
            createNode(zk, path, "", CreateMode.PERSISTENT);
            path = path + "/" + node;
            createNode(zk, path, "", CreateMode.EPHEMERAL);
        } catch (Exception e) {
            log.error("error", e);
        }
    }

    private void createNode(ZooKeeper zk, String path, String data, CreateMode createMode) throws Exception {
        if (zk.exists(path, false) == null) {
            String p = zk.create(path, data.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, createMode);
            log.info("create zookeeper node ({} => {})", p, data);
        }
    }


    public static ServiceRegistry getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        public static final ServiceRegistry INSTANCE = new ServiceRegistry();
    }

    public static void main(String[] args) throws Exception {
        ServiceRegistry serviceRegistry = ServiceRegistry.getInstance();
        serviceRegistry.registerServer(ServiceRegistry.class, "1270.0.0.1:12315", "40");
        serviceRegistry.registerServer(ServiceRegistry.class, "1270.0.0.1:12314", "60");
        Thread.sleep(100000);
    }

}
