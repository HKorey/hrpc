package com.hquery.hrpc.lock;

/**
 * @author hquery.huang
 * 2019/3/25 11:42:01
 */
public interface DistributeLock {

    void lock(Lock lock);

    boolean tryLock(Lock lock);

    boolean tryLock(Lock lock, long lockExpireTime);

    void unlock(Lock lock);
}
