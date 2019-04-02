package com.hquery.hrpc.core.connector;

import com.hquery.hrpc.core.RpcContext;
import com.hquery.hrpc.core.RpcFuture;
import com.hquery.hrpc.core.codec.RpcDecoder;
import com.hquery.hrpc.core.codec.RpcEncoder;
import com.hquery.hrpc.core.model.RpcRequest;
import com.hquery.hrpc.core.model.RpcResponse;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.TimeUnit;


/**
 * SOFA RPC - RpcConnectionFactory
 * <p>
 * Created by HQuery on 2018/12/1.
 */
@Slf4j
public class NettyRpcConnector implements RpcConnector {

    private String host;

    private int port;

    private Channel channel;

    private EventLoopGroup eventLoopGroup;

    private RpcFutureUtil futureUtil = new RpcFutureUtil();

    public NettyRpcConnector(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void init() throws InterruptedException {
        // Configure the client
        eventLoopGroup = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        b.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel) throws Exception {
                        channel.pipeline().addLast(
                                new RpcEncoder(),
                                new RpcDecoder(RpcResponse.class),
                                new ConnectorHandler(futureUtil));
                    }
                });
        // start the client
        ChannelFuture f = b.connect(host, port).sync();
        channel = f.channel();
        // wait until the connection is close.
//            f.channel().closeFuture().sync();
    }

    @Override
    public RpcResponse invoke(RpcRequest request) {
        try {
            return send(request, RpcContext.getContext());
        } finally {
            RpcContext.removeContext();
        }
    }

    @Override
    public RpcConnector setHost(String host) {
        this.host = host;
        return this;
    }

    @Override
    public RpcConnector setPort(int port) {
        this.port = port;
        return this;
    }

    @Override
    public void start() throws Exception {
        init();
    }

    @Override
    public void stop() throws Exception {
        if (eventLoopGroup != null) {
            eventLoopGroup.shutdownGracefully();
        }
    }

    public RpcResponse send(RpcRequest request, RpcContext rpcContext) {
        // One way request, client does not expect response
        if (rpcContext.isOneWay()) {
            channel.writeAndFlush(request);
            return null;
        }
        RpcFuture<Object> future = new RpcFuture<>();
        futureUtil.setRpcFuture(request.getRequestId(), future);
        channel.writeAndFlush(request);
        RpcContext context = RpcContext.getContext();
        if (rpcContext.isAsync()) {
            context.setFuture(future);
            return null;
        }
        try {
            future.await(context.getRpcTimeOutInMillis(), TimeUnit.MILLISECONDS);
            return future.getResponse();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
