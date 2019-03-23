package com.hquery.hrpc.core.protocol;

import com.hquery.hrpc.core.model.RpcCommand;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;


/**
 * Created by HQuery on 2018/12/1.
 */
@Slf4j
public class RpcEncoder extends MessageToByteEncoder<Serializable> {

    private RpcCodec rpcCode;

    public RpcEncoder() {
        rpcCode = RpcCodec.getInstance();
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Serializable msg, ByteBuf out) throws Exception {
        if (!(msg instanceof RpcCommand)) {
            log.warn("msg type [" + msg.getClass() + "] is not subclass of RpcCommand");
            return;
        }
        byte[] bytes = rpcCode.encode(msg);
        out.writeInt(bytes.length);
        out.writeBytes(bytes);
    }

}
