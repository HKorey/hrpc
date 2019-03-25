package com.hquery.hrpc.zookeeper;

public interface LeaderListener {

    /**
     * Callback when become the leader
     */
    void isLeader();
}
