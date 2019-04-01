package com.hquery.hrpc.core.server;

import com.hquery.hrpc.constants.GlobalConstants;
import com.hquery.hrpc.core.Exporter;
import com.hquery.hrpc.core.NettyRpcAcceptor;
import com.hquery.hrpc.core.ServiceRegistry;
import com.hquery.hrpc.init.AbstractServerLifeCycle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.concurrent.*;

/**
 * Created by HQuery on 2018/12/1.
 */
@Slf4j
@Component
public class RpcServer extends AbstractServerLifeCycle {

    public static final int DEFAULT_WEIGHT = 100;

    public static final CountDownLatch NETTY_SERVER_START_BLOCKING = new CountDownLatch(1);

    public static final int NETTY_CONNECTION_TIME_OUT_MINUTES = 1;

    @Resource
    private Exporter exporter;

    @Resource
    private ServiceRegistry serviceRegistry;

    @Resource
    private NettyRpcAcceptor nettyRpcAcceptor;

    @Value("${hrpc.port}")
    private int port;

    private int weight;

    public RpcServer() {
    }

    public void export(Class<?> clazz, Object obj) {
        export(clazz, obj, null);
    }

    public void export(Class<?> clazz, Object obj, String version) {
        try {
            NETTY_SERVER_START_BLOCKING.await(NETTY_CONNECTION_TIME_OUT_MINUTES, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            throw new RuntimeException("netty connect time out", e);
        }
        exporter.export(clazz, obj, version);
        serviceRegistry.registerServer(clazz, GlobalConstants.DEFAULT_LOCAL_HOST + ":" + port, "" + weight);
    }


    @Override
    public void start() {
        log.info("获取本地服务IP【{}:{}】", GlobalConstants.DEFAULT_LOCAL_HOST, port);
        this.weight = DEFAULT_WEIGHT;
        try {
            nettyRpcAcceptor.setCountDownLatch(NETTY_SERVER_START_BLOCKING);
            nettyRpcAcceptor.init();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
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
