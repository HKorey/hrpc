package com.hquery.hrpc.core.client;

import com.hquery.hrpc.core.connector.RpcConnector;
import com.hquery.hrpc.core.proxy.RpcProxy;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by HQuery on 2018/12/1.
 */
@Data
@Slf4j
public class RpcClient {

    private RpcProxy rpcProxy;

    private RpcConnector rpcConnector;

    public static final RpcClient getInstance() {
        return new RpcClient();
    }

    public void init(String host, int port) {
        this.rpcProxy = new RpcProxy();
//        this.rpcConnector = new Nett

    }
}
