package com.hquery.hrpc.core;

import com.hquery.hrpc.core.model.RpcRequest;
import com.hquery.hrpc.core.model.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

/**
 * @author hquery.huang
 * 2019/3/23 17:25:01
 */
@Slf4j
public class AcceptorHandler extends SimpleChannelInboundHandler<RpcRequest> {

    private RpcProcessor rpcProcessor;

    public AcceptorHandler(RpcProcessor rpcProcessor) {
        this.rpcProcessor = rpcProcessor;
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        log.info("channelUnregistered");
        ctx.fireChannelUnregistered();
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
    public void exceptionCaught(
            ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.info("Unexpected exception from downstream.", cause);
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest msg) throws Exception {
        processRequest(ctx, msg);
    }

    private void processRequest(final ChannelHandlerContext ctx, final RpcRequest request) {
        //request 预处理，判断是否是心跳。
        // TODO
        rpcProcessor.submit(new Runnable() {
            @Override
            public void run() {
                hand(ctx, request);
            }
        });
    }

    public void hand(final ChannelHandlerContext ctx, RpcRequest request) {
        Object obj = rpcProcessor.findService(request.getClassName());
        if (obj == null) {
            throw new IllegalArgumentException("has no these class");
        }
        RpcResponse response = new RpcResponse();
        response.setRequestId(request.getRequestId());
        try {
            Method m = obj.getClass().getMethod(request.getMethodName(), request.getParameterTypes());
            Object result = m.invoke(obj, request.getParameters());
            response.setResult(result);
        } catch (Throwable t) {
            log.error("error", t);
            response.setError(t);
        }
        ctx.writeAndFlush(response);
    }
}
