package com.hquery.hrpc.core.connector;

import com.hquery.hrpc.core.model.RpcRequest;
import com.hquery.hrpc.core.model.RpcResponse;
import com.hquery.hrpc.core.protocol.RpcDecoder;
import com.hquery.hrpc.core.protocol.RpcEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.nio.channels.Channel;

/**
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

    @Override
    public RpcResponse invoke(RpcRequest request) {
        // Configure the client
        eventLoopGroup = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        b.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
        .handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline().addLast(
                        new RpcEncoder(),
                        new RpcDecoder(),
                        new ConnectorHandler(futureUtil);
                )
            }
        });


        return null;
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }
}
