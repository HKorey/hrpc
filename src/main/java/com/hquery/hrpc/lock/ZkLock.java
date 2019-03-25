package com.hquery.hrpc.lock;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;

import java.util.concurrent.TimeUnit;

/**
 * @author hquery.huang
 * 2019/3/25 11:43:51
 */
@Slf4j
public class ZkLock implements DistributeLock {

    /**
     * 单个业务持有锁的时间20s,防止死锁
     */
    private final static long LOCK_EXPIRE = 20 * 1000L;

    /**
     * 500ms拿不到锁就，自动失败
     */
    private final static long LOCK_WAIT_TIME = 500L;

    /**
     * 前缀
     */
    public static final String PREFIX = "/locks";

    /**
     * Lock the path
     */
    @Override
    public void lock(Lock lock) {
        InterProcessMutex mutex = new InterProcessMutex(lock.getClient(), PREFIX + lock.getBizKey());
        try {
            mutex.acquire();
        } catch (Exception e) {
            log.error("尝试加ZK分布式锁失败", e);
        }
    }

    @Override
    public boolean tryLock(Lock lock) {
        return tryLock(lock, LOCK_EXPIRE);
    }

    /**
     * Lock the path with timeout
     * @param lockExpireTime timeout(ms)
     * @return lock successfully or not
     */
    @Override
    public boolean tryLock(Lock lock, long lockExpireTime) {
        InterProcessMutex mutex = new InterProcessMutex(lock.getClient(), PREFIX + lock.getBizKey());
        try {
            return mutex.acquire(lockExpireTime, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            log.error("尝试加ZK分布式锁失败", e);
        }
        return false;
    }

    /**
     * Unlock the path
     */
    @Override
    public void unlock(Lock lock) {
        InterProcessMutex mutex = new InterProcessMutex(lock.getClient(), PREFIX + lock.getBizKey());
        try {
            mutex.release();
        } catch (Exception e) {
            throw new RuntimeException("failed to unlock: " + mutex);
        }
    }
}
