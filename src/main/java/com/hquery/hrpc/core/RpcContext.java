package com.hquery.hrpc.core;

import com.hquery.hrpc.constants.GlobalConstants;

/**
 * @author hquery.huang
 * 2019/3/21 15:34:29
 */
public class RpcContext {

    private static final ThreadLocal<RpcContext> RPC_CONTEXT_THREAD_LOCAL = ThreadLocal.withInitial(() -> {
        RpcContext rpcContext = new RpcContext();
        rpcContext.setRpcTimeOutInMillis(GlobalConstants.RPC_TIMEOUT);
        rpcContext.setOneWay(false);
        rpcContext.setAsync(false);
        return rpcContext;
    });

    /**
     * RPC 超时时间
     */
    private int rpcTimeOutInMillis;

    private boolean oneWay;

    private boolean async;

    private RpcFuture<?> future;

    private InvokeCallback callback;

    private RpcContext() {
    }

    public static RpcContext getContext() {
        return RPC_CONTEXT_THREAD_LOCAL.get();
    }

    public static void removeContext() {
        RPC_CONTEXT_THREAD_LOCAL.remove();
    }

    public int getRpcTimeOutInMillis() {
        return rpcTimeOutInMillis;
    }

    public void setRpcTimeOutInMillis(int rpcTimeOutInMillis) {
        this.rpcTimeOutInMillis = rpcTimeOutInMillis;
    }

    public boolean isOneWay() {
        return oneWay;
    }

    public void setOneWay(boolean oneWay) {
        this.oneWay = oneWay;
    }

    public boolean isAsync() {
        return async;
    }

    public void setAsync(boolean async) {
        this.async = async;
    }

    public RpcFuture<?> getFuture() {
        return future;
    }

    public void setFuture(RpcFuture<?> future) {
        this.future = future;
        if (callback != null) {
            future.setCallback(callback);
        }
    }

    public InvokeCallback getCallback() {
        return callback;
    }

    public void setCallback(InvokeCallback callback) {
        this.callback = callback;
        if (future != null) {
            future.setCallback(callback);
        }
    }
}
