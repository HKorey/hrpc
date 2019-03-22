package com.hquery.hrpc.core.connector;

import com.hquery.hrpc.core.RpcContext;
import com.hquery.hrpc.core.RpcFuture;
import com.hquery.hrpc.core.model.RpcRequest;
import com.hquery.hrpc.core.model.RpcResponse;
import com.hquery.hrpc.core.protocol.RpcDecoder;
import com.hquery.hrpc.core.protocol.RpcEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;


/**
 *
 * SOFA RPC - RpcConnectionFactory
 *
 * Created by HQuery on 2018/12/1.
 */
@Slf4j
@Data
public class NettyRpcConnector implements RpcConnector {

    private String host;

    private int port;

    private Channel channel;

    private EventLoopGroup eventLoopGroup;

    private RpcFutureUtil futureUtil = new RpcFutureUtil();

    public void init() {
        // Configure the client
        eventLoopGroup = new NioEventLoopGroup();
        try {
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
//                    .handler(new ChannelInitializer<SocketChannel>() {
//                        @Override
//                        protected void initChannel(SocketChannel socketChannel) throws Exception {
//                            socketChannel.pipeline().addLast(
//                                    new RpcEncoder(),
//                                    new RpcDecoder(),
//                                    new ConnectorHandler(futureUtil);
//                )
//                        }
//                    });
        } catch (Exception e) {
            log.error("error", e);
        }
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
    public void start() {
        init();
    }

    @Override
    public void stop() {
        eventLoopGroup.shutdownGracefully();
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
