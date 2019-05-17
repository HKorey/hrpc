package com.hquery.hrpc.core.registry;

import com.hquery.hrpc.constants.GlobalConstants;
import com.hquery.hrpc.core.discover.ServiceDiscovery;
import com.hquery.hrpc.core.exception.RpcException;
import com.hquery.hrpc.init.AbstractServerLifeCycle;
import com.hquery.hrpc.utils.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author hquery.huang
 * 2019/5/17 16:33:04
 */
@Slf4j
@Component
public class InitRegistryProtocol extends AbstractServerLifeCycle {

    @Value("${hrpc.server.registry.protocol.client:zookeeper}")
    private String registryProtocol;

    @Value("${hrpc.route.type:random}")
    private String routeType;

    @Override
    public int order() {
        return Integer.MIN_VALUE;
    }

    @Override
    public void start() {
        DefaultRegistry registry = SpringContextUtil.getBean(registryProtocol + GlobalConstants.REGISTRY_PROTOCOL_SUFFIX);
        if (registry == null) {
            throw new RpcException("未找到注册协议，请配置hrpc.server.registry.protocol.client");
        }
        for (Class service : ServiceDiscovery.getAllServices()) {
            registry.registerClient(service, GlobalConstants.DEFAULT_LOCAL_HOST);
        }
    }
}
