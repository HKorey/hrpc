package com.hquery.hrpc.zookeeper;

import lombok.Getter;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author hquery.huang
 * 2019/4/3 21:09:29
 */
@Component
@Lazy
public class HrpcZkClient implements DisposableBean {

    @Value("${hrpc.zk,servers}")
    private String zkServers;

    @Getter
    private ZkClient client;

    @PostConstruct
    public void init() {
        client = ZkClient.newClient(zkServers, ZkConstants.ZK_NAME_SPACE);
    }

    @Override
    public void destroy() throws Exception {
        if (client != null) {
            client.shutdown();
        }
    }

}
