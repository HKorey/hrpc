package com.hquery.hrpc.core.protocol;

import com.hquery.hrpc.constants.GlobalConstants;

import java.io.IOException;

/**
 * Created by HQuery on 2018/12/1.
 */
public class RpcCodec {

    private Serializer serialize;

    private RpcCodec() {
        serialize = GlobalConstants.SERIALIZER;
    }

    public RpcCodec(Serializer serialize) {
        this.serialize = serialize;
    }

    byte[] encode(Object obj) throws IOException {
        return serialize.serialize(obj);
    }

    public <T> T decode(byte[] bytes, Class<T> clazz) throws IOException {
        return serialize.deserialize(bytes, clazz);
    }

    public static RpcCodec getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        public static final RpcCodec INSTANCE = new RpcCodec();
    }

}
