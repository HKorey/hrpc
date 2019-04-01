package com.hquery.hrpc.init;

/**
 * @author hquery.huang
 * 2019/3/30 17:59
 */
public interface ServerLifeCycle {

    /**
     * @author hquery
     * 2019/3/30 20:09
     * @description start
     * @return void
     */
    void start();

    /**
     * @author hquery
     * 2019/3/30 20:09
     * @description stop
     * @return void
     */
    void stop();

    /**
     * @author hquery
     * 2019/3/30 20:09
     * @description isStarted
     * @param
     * @return boolean
     */
    boolean isStarted();
}
