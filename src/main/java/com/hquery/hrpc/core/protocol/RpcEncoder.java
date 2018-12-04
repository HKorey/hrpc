package com.hquery.hrpc.core.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.io.Serializable;

/**
 * Created by HQuery on 2018/12/1.
 */
public class RpcEncoder extends MessageToByteEncoder<Serializable> {

    private RpcCode rpcCode;

    public RpcEncoder() {
        rpcCode = RpcCode.getInstance();
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Serializable serializable, ByteBuf byteBuf) throws Exception {
//        rpcCode.encode()
    }
}
