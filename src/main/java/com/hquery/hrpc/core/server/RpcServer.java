package com.hquery.hrpc.core.server;

import com.hquery.hrpc.core.Exporter;
import com.hquery.hrpc.core.NettyRpcAcceptor;
import com.hquery.hrpc.core.RpcAcceptor;
import com.hquery.hrpc.core.RpcProcessor;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by HQuery on 2018/12/1.
 */
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
        this.exporter = Exporter.getInstance();
        this.processor = new RpcProcessor(exporter);
        this.acceptor = new NettyRpcAcceptor();
        this.acceptor.setHost
    }

}
