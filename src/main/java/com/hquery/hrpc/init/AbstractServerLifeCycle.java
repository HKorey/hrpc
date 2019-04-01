package com.hquery.hrpc.init;

import org.springframework.context.ApplicationContext;

/**
 * @author hquery
 * 2019/3/30 19:47
 * @email hquery@163.com
 */
public abstract class AbstractServerLifeCycle implements ServerLifeCycle {

    protected static volatile ApplicationContext applicationContext;

    /**
     * @author hquery
     * 2019/3/30 19:48
     * @description 用来排序
     * @param
     * @return int
     */
    public abstract int order();

    public final static void setApplicationContext(ApplicationContext applicationContext) {
        if (AbstractServerLifeCycle.applicationContext == null) {
            synchronized (AbstractServerLifeCycle.class) {
                if (AbstractServerLifeCycle.applicationContext == null) {
                    AbstractServerLifeCycle.applicationContext = applicationContext;
                }
            }
        }
    }

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
