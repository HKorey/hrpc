package com.hquery.hrpc.core.server;

import com.hquery.hrpc.constants.GlobalConstants;
import com.hquery.hrpc.core.acceptor.NettyRpcAcceptor;
import com.hquery.hrpc.core.exception.RpcException;
import com.hquery.hrpc.core.registry.DefaultRegistry;
import com.hquery.hrpc.init.AbstractServerLifeCycle;
import com.hquery.hrpc.utils.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * RPC Server
 *
 * @author hquery
 * 2019/4/2 10:26:02
 */
@Slf4j
@Component
@DependsOn({"exporter", "springContextUtil"})
public class RpcServer extends AbstractServerLifeCycle {

    public static final int DEFAULT_WEIGHT = 100;

    public static final int NETTY_CONNECTION_TIME_OUT_MINUTES = 1;

    @Resource
    private Exporter exporter;

    @Resource
    private NettyRpcAcceptor nettyRpcAcceptor;

    @Value("${hrpc.server.registry.protocol.client:zookeeper}")
    private String registryProtocol;

    private int weight = DEFAULT_WEIGHT;

    public void export(Class<?> clazz, Object obj) {
        export(clazz, obj, null);
    }

    public void export(Class<?> clazz, Object obj, String version) {
        exporter.export(clazz, obj, version);
        // 注册至注册中心
        DefaultRegistry registry = SpringContextUtil.getBean(registryProtocol + GlobalConstants.REGISTRY_PROTOCOL_SUFFIX);
        if (registry == null) {
            throw new RpcException("未找到注册协议，请配置hrpc.server.registry.protocol.client");
        }
        registry.registerServer(clazz, GlobalConstants.DEFAULT_LOCAL_HOST + ":" + GlobalConstants.DEFAULT_HRPC_PORT, String.valueOf(weight));
    }


    @Override
    public void start() {
        log.info("获取本地服务IP【{}:{}】", GlobalConstants.DEFAULT_LOCAL_HOST, GlobalConstants.DEFAULT_HRPC_PORT);
        try {
            nettyRpcAcceptor.init();
        } catch (InterruptedException e) {
            log.error("初始化Netty出现异常", e);
        } catch (IOException e) {
            log.error("初始化Netty出现异常", e);
        }
    }

    @Override
    public void stop() {
        nettyRpcAcceptor.getBossGroup().shutdownGracefully();
        nettyRpcAcceptor.getWorkerGroup().shutdownGracefully();
    }

    @Override
    public int order() {
        return 0;
    }

}
