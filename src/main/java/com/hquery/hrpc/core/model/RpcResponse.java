package com.hquery.hrpc.core.model;


/**
 * Created by HQuery on 2018/12/1.
 */
public class RpcResponse extends RpcCommand {

    private static final long serialVersionUID = -1225076555107228476L;

    private long requestId;

    private Throwable error;

    private Object result;

    public RpcResponse() {
    }

    public long getRequestId() {
        return requestId;
    }

    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }

    public Throwable getError() {
        return error;
    }

    public void setError(Throwable error) {
        this.error = error;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
