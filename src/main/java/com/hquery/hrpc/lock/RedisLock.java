package com.hquery.hrpc.lock;

import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author hquery.huang
 * 2019/3/25 12:06:45
 */
@Slf4j
public class RedisLock implements DistributeLock {

    /**
     * 单个业务持有锁的时间20s,防止死锁
     */
    private final static long LOCK_EXPIRE = 20 * 1000L;

    /**
     * 500ms拿不到锁就，自动失败
     */
    private final static long LOCK_WAIT_TIME = 500L;

    @Resource
    private RedissonClient redissonClient;

    @Override
    public void lock(Lock lock) {
        RLock redLock = redissonClient.getLock(lock.getBizKey());
        redLock.lock();
        lock.setRLock(redLock);
    }

    @Override
    public boolean tryLock(Lock lock) {
        return tryLock(lock, LOCK_EXPIRE);
    }

    @Override
    public boolean tryLock(Lock lock, long lockExpireTime) {
        RLock redLock = redissonClient.getLock(lock.getBizKey());
        try {
            return redLock.tryLock(LOCK_WAIT_TIME, lockExpireTime, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            log.error("尝试加redisson锁失败", e);
        }
        return false;
    }

    @Override
    public void unlock(Lock lock) {
        lock.getRLock().unlock();
    }

    public static void main(String[] args) {
        Config config = new Config();
        config.useSentinelServers()
                .addSentinelAddress("127.0.0.1:6379", "127.0.0.1:6369", "127.0.0.1:6389")
                .setMasterName("masterName")
                .setPassword("passwd")
                .setDatabase(0);
//        config.useClusterServers()
//                .addNodeAddress("127.0.0.1:6379", "127.0.0.1:6369", "127.0.0.1:6389")
//                .setPassword("passwd");
        RedissonClient redissonClient = Redisson.create(config);
        RLock redLock = redissonClient.getLock("REDLOCK_KEY");
        boolean isLock;
        try {
            isLock = redLock.tryLock();
            // 500ms拿不到锁, 就认为获取锁失败。10000ms即10s是锁失效时间。
            isLock = redLock.tryLock(500, 10000, TimeUnit.MILLISECONDS);
            if (isLock) {
                //TODO if get lock success, do something;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            redLock.unlock();
        }
    }
}
