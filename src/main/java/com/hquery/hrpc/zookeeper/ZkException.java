package com.hquery.hrpc.zookeeper;

/**
 * ZK异常
 *
 * @author hquery
 * 2019/4/3 20:13:43
 */
public class ZkException extends RuntimeException {

    public ZkException() {
        super();
    }

    public ZkException(String message) {
        super(message);
    }

    public ZkException(String message, Throwable cause) {
        super(message, cause);
    }

    public ZkException(Throwable cause) {
        super(cause);
    }

    protected ZkException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
