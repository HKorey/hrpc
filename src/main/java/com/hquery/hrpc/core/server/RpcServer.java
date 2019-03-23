package com.hquery.hrpc.core.server;

import com.hquery.hrpc.core.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by HQuery on 2018/12/1.
 */
@Slf4j
public class RpcServer {

    protected ConcurrentHashMap<String, Object> serviceEngine = new ConcurrentHashMap<>();

    private RpcProcessor processor;

    private RpcAcceptor acceptor;

    private Exporter exporter;

    private String host;

    private int port;

    private int weight;

    private boolean registry = false;

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
}
