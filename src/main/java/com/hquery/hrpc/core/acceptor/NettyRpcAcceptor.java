package com.hquery.hrpc.core.acceptor;

import com.hquery.hrpc.constants.GlobalConstants;
import com.hquery.hrpc.core.processor.RpcProcessor;
import com.hquery.hrpc.core.codec.RpcDecoder;
import com.hquery.hrpc.core.codec.RpcEncoder;
import com.hquery.hrpc.core.model.RpcRequest;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @author hquery.huang
 * 2019/3/23 15:56:42
 */
@Slf4j
@Component
public class NettyRpcAcceptor implements RpcAcceptor {

    @Resource
    private RpcProcessor rpcProcessor;

    @Getter
    private EventLoopGroup bossGroup;

    @Getter
    private EventLoopGroup workerGroup;

    public void init() throws IOException, InterruptedException {
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();

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
                                new AcceptorHandler(rpcProcessor));
                    }
                });
        String host = GlobalConstants.DEFAULT_LOCAL_HOST;
        // Start the server.
        b.bind(host, GlobalConstants.DEFAULT_HRPC_PORT).sync();
//            ChannelFuture f =  b.bind(host,port).sync();
        // Wait until the server socket is closed.
        //  f.channel().closeFuture().sync();
        log.info("started and listen on【{}:{}】", host, GlobalConstants.DEFAULT_HRPC_PORT);
    }

}
