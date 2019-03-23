package com.hquery.hrpc.core.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * Created by HQuery on 2018/12/1.
 */
public class RpcDecoder extends LengthFieldBasedFrameDecoder {

    private Class<?> clazz;

    private RpcCodec codec;

    public RpcDecoder(Class<?> clazz) {
        super(1048576,0, 4, 0, 4);
        this.clazz = clazz;
        this.codec = RpcCodec.getInstance();
    }

    public RpcDecoder(int maxFramlength, Class<?> clazz) {
        super(maxFramlength, 0, 4, 0, 4);
        this.clazz = clazz;
        this.codec = RpcCodec.getInstance();
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        ByteBuf frame = (ByteBuf) super.decode(ctx, in);
        if (frame == null) {
            return null;
        }
        int dataLength = frame.readableBytes();
        byte[] data = new byte[dataLength];
        frame.readBytes(data);
        return codec.decode(data, clazz);
    }
}
