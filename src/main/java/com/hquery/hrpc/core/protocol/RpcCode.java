package com.hquery.hrpc.core.protocol;

import java.io.IOException;

/**
 * Created by HQuery on 2018/12/1.
 */
public class RpcCode {

    private static final RpcCode INSTANCE = new RpcCode();

    private Serializer serialize;

    private RpcCode() {
    }

    public RpcCode(Serializer serialize) {
        this.serialize = serialize;
    }

    byte[] encode(Object obj) throws IOException {
        return serialize.serialize(obj);
    }

    public <T> T decode(byte[] bytes, Class<T> clazz) throws IOException {
        return serialize.deserialize(bytes, clazz);
    }

    public static RpcCode getInstance() {
        return INSTANCE;
    }

}
