package com.hquery.hrpc.core.server;

import com.hquery.hrpc.core.*;
import com.hquery.hrpc.init.AbstractServerLifeCycle;
import com.hquery.hrpc.init.ServerLifeCycle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by HQuery on 2018/12/1.
 */
@Slf4j
@Component
public class RpcServer extends AbstractServerLifeCycle {

    protected ConcurrentHashMap<String, Object> serviceEngine = new ConcurrentHashMap<>();

    private RpcProcessor processor;

    private RpcAcceptor acceptor;

    private Exporter exporter;

    private String host;

    @Value("${server.port}")
    private int port;

    private int weight;

    private boolean registry = false;

    private boolean started;

    public RpcServer() {
    }

    public RpcServer(String host, int port) {
        this(host, port, 100);
    }

    public RpcServer(String host, int port, int weight) {
        this.host = host;
        this.port = port;
        this.weight = weight;
        exporter = Exporter.getInstance();
        acceptor = new NettyRpcAcceptor();
        acceptor.setHost(host);
        acceptor.setPort(port);
        processor = new RpcProcessor(exporter);
        acceptor.setProcessor(processor);
        try {
            acceptor.start();
        } catch (IOException e) {
            log.error("error", e);
        }
    }

    public void setRegistry(boolean registry) {
        this.registry = registry;
    }

    public void export(Class<?> clazz, Object obj) {
        export(clazz, obj, null);
    }

    public void export(Class<?> clazz, Object obj, String version) {
        exporter.export(clazz, obj, version);
        if (registry) {
            ServiceRegistry.getInstance().registerServer(clazz, host + ":" + port, "" + weight);
        }
    }


    @Override
    public void start() {
        try {
            String hostAddress = InetAddress.getLocalHost().getHostAddress();
            log.info("获取本地服务IP【{}:{}】", hostAddress, port);
        } catch (UnknownHostException e) {
            log.error("获取本地服务IP失败", e);
        }
    }

    @Override
    public boolean isStarted() {
        return started;
    }

    @Override
    public int order() {
        return 0;
    }
}
