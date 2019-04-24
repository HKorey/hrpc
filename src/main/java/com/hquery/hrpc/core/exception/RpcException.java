package com.hquery.hrpc.core.exception;

/**
 * RPC异常
 *
 * @author hquery
 * 2019/4/3 20:10:30
 */
public class RpcException extends RuntimeException {

    private static final long serialVersionUID = 6238589897120159526L;

    public RpcException() {
        super();
    }

    public RpcException(String message) {
        super(message);
    }

    public RpcException(String message, Throwable thr) {
        super(message, thr);
    }

    public RpcException(Throwable thr) {
        super(thr);
    }

}
