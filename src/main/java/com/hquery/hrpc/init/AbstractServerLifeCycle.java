package com.hquery.hrpc.init;

/**
 * @author hquery
 * 2019/3/30 19:47
 * @email hquery@163.com
 */
public abstract class AbstractServerLifeCycle implements ServerLifeCycle {

    /**
     * 排序
     *
     * @param
     * @return int
     * @author hquery
     * 2019/4/1 19:13:47
     */
    public abstract int order();

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public boolean isStarted() {
        return true;
    }

}
