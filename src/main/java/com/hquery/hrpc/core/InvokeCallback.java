package com.hquery.hrpc.core;

/**
 * @author hquery.huang
 * 2019/3/21 15:51:12
 */
public interface InvokeCallback {

    /**
     * onResponse
     * @param result
     */
    void onSuccess(Object result);

    /**
     * onException
     * @param throwable
     */
    void onError(Throwable throwable);
}
