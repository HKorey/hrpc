package com.hquery.hrpc.core.connector;

import com.hquery.hrpc.core.invoker.InvokeCallback;
import com.hquery.hrpc.core.model.RpcResponse;

import java.util.concurrent.*;

/**
 * @author hquery.huang
 * 2019/3/21 15:49:35
 */
public class RpcFuture<V> implements Future<V> {

    private volatile RpcResponse response;

    private volatile Throwable cause;

//    private volatile int waiters;

    private volatile InvokeCallback callback;

    private final CountDownLatch countDownLatch;

    public RpcFuture() {
        this.countDownLatch = new CountDownLatch(1);
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public boolean isDone() {
        return countDownLatch.getCount() <= 0L;
    }

    @Override
    public V get() throws InterruptedException, ExecutionException {
        try {
            return get(Long.MAX_VALUE, TimeUnit.DAYS);
        } catch (TimeoutException e) {
            throw new InterruptedException(e.getMessage());
        }
    }

    @Override
    public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
//        await(timeout, unit);
        countDownLatch.await(timeout, unit);
        try {
            return (V) getResponse().getResult();
        } catch (Exception e) {
            throw new ExecutionException(e);
        }
    }

    public RpcResponse getResponse() {
        return response;
    }

    public void setResponse(RpcResponse response) {
        this.response = response;
        countDownLatch.countDown();
    }

    public InvokeCallback getCallback() {
        return callback;
    }

    public void setCallback(InvokeCallback callback) {
        this.callback = callback;
    }

    public CountDownLatch getCountDownLatch() {
        return countDownLatch;
    }

    public Throwable getCause() {
        return cause;
    }

    public void setCause(Throwable cause) {
        this.cause = cause;
    }

    public boolean await(long timeout, TimeUnit unit) throws InterruptedException {
        countDownLatch.await();
        return true;
    }


    //    private boolean await(long timeout, TimeUnit unit) throws InterruptedException {
//        long timeOutMillis = unit.toMillis(timeout);
//        long endTime = System.currentTimeMillis() + timeOutMillis;
//        synchronized (this) {
//            if (done) {
//                return done;
//            }
//            if (timeOutMillis <= 0) {
//                return done;
//            }
//            waiters++;
//            try {
//                while (!done) {
//                    wait(timeOutMillis);
//                    if (endTime < System.currentTimeMillis() && !done) {
//                        exception = new TimeoutException("time out");
//                        break;
//                    }
//                }
//            } finally {
//                waiters--;
//            }
//        }
//        return done;
//    }
}
