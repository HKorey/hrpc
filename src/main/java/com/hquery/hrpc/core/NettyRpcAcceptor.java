package com.hquery.hrpc.core;

import com.hquery.hrpc.core.model.RpcRequest;
import com.hquery.hrpc.core.protocol.RpcDecoder;
import com.hquery.hrpc.core.protocol.RpcEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @author hquery.huang
 * 2019/3/23 15:56:42
 */
@Slf4j
public class NettyRpcAcceptor implements RpcAcceptor {

    private String host;

    private int port;

    private RpcProcessor processor;

    private EventLoopGroup bossGroup;

    private EventLoopGroup workerGroup;

    public String getHost() {
        return host;
    }

    public void init() throws IOException {
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(
                                    new RpcEncoder(),
                                    new RpcDecoder(RpcRequest.class),
                                    new AcceptorHandler(NettyRpcAcceptor.this));
                        }
                    });


        } catch (Exception e) {
            log.error("error", e);
        }
    }

    @Override
    public void setHost(String host) {
        this.host = host;
    }

    @Override
    public void start() throws IOException {
        this.init();
    }

    @Override
    public void stop() throws IOException {
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }

    public int getPort() {
        return port;
    }

    @Override
    public void setPort(int port) {
        this.port = port;
    }

    public RpcProcessor getProcessor() {
        return processor;
    }

    @Override
    public void setProcessor(RpcProcessor processor) {
        this.processor = processor;
    }

    public EventLoopGroup getBossGroup() {
        return bossGroup;
    }

    public void setBossGroup(EventLoopGroup bossGroup) {
        this.bossGroup = bossGroup;
    }

    public EventLoopGroup getWorkerGroup() {
        return workerGroup;
    }

    public void setWorkerGroup(EventLoopGroup workerGroup) {
        this.workerGroup = workerGroup;
    }
}
