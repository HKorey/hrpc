package com.hquery.hrpc.core.connector;

import com.hquery.hrpc.core.model.RpcRequest;
import com.hquery.hrpc.core.model.RpcResponse;

import java.io.IOException;

/**
 * Created by HQuery on 2018/12/1.
 */
public interface RpcConnector {

    /**
     * 执行调用
     *
     * @param request
     * @return com.hquery.hrpc.core.model.RpcResponse
     * @author hquery
     * 2019/4/2 16:35:06
     */
    RpcResponse invoke(RpcRequest request);

    /**
     * 设置域名
     *
     * @param host
     * @return void
     * @author hquery
     * 2019/4/2 18:03:44
     */
    RpcConnector setHost(String host);

    /**
     * 设置端口
     *
     * @param port
     * @return void
     * @author hquery
     * 2019/4/2 18:03:48
     */
    RpcConnector setPort(int port);

    /**
     * 开启链接
     *
     * @param
     * @return void
     * @author hquery
     * 2019/4/2 18:03:51
     */
    void start() throws Exception;

    /**
     * 关闭链接
     *
     * @param
     * @return void
     * @author hquery
     * 2019/4/2 18:03:57
     */
    void stop() throws Exception;
}
