package com.hquery.hrpc.core.codec;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.caucho.hessian.io.SerializerFactory;
import com.hquery.hrpc.core.codec.anthession.SingleClassLoaderSofaSerializerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by HQuery on 2018/12/1.
 */
public class Hessian2Serializer implements Serializer {

    // 摘自 com.alipay.sofa.rpc.codec.bolt.SofaRpcSerialization
    protected SerializerFactory serializerFactory = new SingleClassLoaderSofaSerializerFactory();

    @Override
    public byte[] serialize(Object data) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Hessian2Output out = new Hessian2Output(bos);
        out.setSerializerFactory(serializerFactory);
        out.writeObject(data);
        out.flush();
        return bos.toByteArray();
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        Hessian2Input input = new Hessian2Input(bis);
        return (T) input.readObject(clazz);
    }
}
