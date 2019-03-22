package com.hquery.hrpc.core.protocol;

import com.hquery.hrpc.core.model.RpcResponse;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * Created by HQuery on 2018/12/1.
 */
public class RpcDecoder extends LengthFieldBasedFrameDecoder {


    public RpcDecoder(Class<RpcResponse> rpcResponseClass) {
        super(1048576,0, 4, 0, 4);
    }
}
