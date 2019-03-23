package com.hquery.hrpc.core.codec;

import java.io.IOException;

/**
 * Created by HQuery on 2018/12/1.
 */
public interface Serializer {

    <T> byte[] serialize(T obj) throws IOException;

    <T> T deserialize(byte[] bytes, Class<T> clazz) throws IOException;
}
