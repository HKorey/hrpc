package com.hquery.hrpc.core.connector;

import com.hquery.hrpc.core.InvokeCallback;
import com.hquery.hrpc.core.RpcFuture;
import com.hquery.hrpc.core.model.RpcResponse;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用以存储回调信息
 * Created by HQuery on 2018/12/1.
 */
public class RpcFutureUtil {

    private Map<Long, RpcFuture<?>> futures = new ConcurrentHashMap<>();

    public void setRpcFuture(long mid, RpcFuture<?> future) {
        futures.put(mid, future);
    }

    public void notifyRpcMessage(RpcResponse msg) {
        RpcFuture<?> future = futures.remove(msg.getRequestId());
        if (future == null) {
            return;
        }
        future.setResponse(msg);
        //执行回调
        InvokeCallback callback = future.getCallback();
        if (callback == null) {
            return;
        }
        if (msg.getError() != null) {
            callback.onError(msg.getError());
        } else {
            callback.onSuccess(msg.getResult());
        }
    }

    public void notifyRpcException(Exception e) {
        for (RpcFuture<?> future : futures.values()) {
            future.setCause(e);
        }
    }

}
