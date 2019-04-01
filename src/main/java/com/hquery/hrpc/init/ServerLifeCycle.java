package com.hquery.hrpc.init;

/**
 * @author hquery.huang
 * 2019/3/30 17:59
 */
public interface ServerLifeCycle {

    /**
     * start
     *
     * @param
     * @return void
     * @author hquery
     * 2019/4/1 19:17:25
     */
    void start();

    /**
     * stop
     *
     * @param
     * @return void
     * @author hquery
     * 2019/4/1 19:17:44
     */
    void stop();

    /**
     * started success or no
     *
     * @param
     * @return boolean
     * @author hquery
     * 2019/4/1 19:18:12
     */
    boolean isStarted();
}
