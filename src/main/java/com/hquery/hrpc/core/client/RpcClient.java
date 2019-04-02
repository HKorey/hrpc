package com.hquery.hrpc.core.client;

import com.hquery.hrpc.core.connector.RpcConnector;
import com.hquery.hrpc.core.proxy.RpcProxy;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

/**
 * @author hquery.huang
 * 2019/4/2 16:57:46
 */
@Slf4j
@Builder
public class RpcClient {

    private RpcProxy proxy;

    private RpcConnector rpcConnector;
}
