package com.hquery.hrpc.core.connector;

import com.hquery.hrpc.core.model.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by HQuery on 2018/12/1.
 */
public class ConnectorHandler extends SimpleChannelInboundHandler<RpcResponse> {

    public ConnectorHandler() {
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponse msg) throws Exception {

    }

    public ConnectorHandler(RpcFutureUtil futureUtil) {


    }


}
