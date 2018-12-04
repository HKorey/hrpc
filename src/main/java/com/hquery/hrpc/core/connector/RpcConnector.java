package com.hquery.hrpc.core.connector;

import com.hquery.hrpc.core.model.RpcRequest;
import com.hquery.hrpc.core.model.RpcResponse;

/**
 * Created by HQuery on 2018/12/1.
 */
public interface RpcConnector {

    RpcResponse invoke(RpcRequest request);

    void start();

    void stop();

}
