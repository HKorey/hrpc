package com.hquery.hrpc.core.connector;

import com.hquery.hrpc.core.model.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by HQuery on 2018/12/1.
 */
@Slf4j
public class ConnectorHandler extends SimpleChannelInboundHandler<RpcResponse> {

    private RpcFutureUtil futureUtil;

    public ConnectorHandler(RpcFutureUtil futureUtil) {
        this.futureUtil = futureUtil;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponse msg) throws Exception {
        futureUtil.notifyRpcMessage(msg);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // Send the first message if this handler is a client-side handler.
        log.info("channelActive");
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.info("Unexpected exception from downstream.", cause);
        futureUtil.notifyRpcException(new Exception(cause));
        ctx.close();
    }
}
