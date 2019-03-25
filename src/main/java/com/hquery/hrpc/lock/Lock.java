package com.hquery.hrpc.lock;


import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.apache.curator.framework.CuratorFramework;
import org.redisson.api.RLock;

/**
 * @author Administrator
 */
@Data
@Accessors(chain = true)
@ToString
public class Lock {

    private String bizKey;

    private String bizValue;

    /**
     * redis锁
     */
    private RLock rLock;

    /**
     * ZK锁
     */
    private CuratorFramework client;

}
