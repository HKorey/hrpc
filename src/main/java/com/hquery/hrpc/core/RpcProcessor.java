package com.hquery.hrpc.core;

import com.hquery.hrpc.constants.GlobalConstants;
import com.hquery.hrpc.init.AbstractServerLifeCycle;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author hquery.huang
 * 2019/3/23 15:49:43
 */
@Component
public class RpcProcessor extends AbstractServerLifeCycle {

    private ExecutorService executorService;

    @Resource
    private Exporter exporter;

    public void submit(Runnable runnable) {
        executorService.submit(runnable);
    }

    public Object findService(String clazzName) {
        return findService(clazzName, null);
    }

    public Object findService(String clazzName, String version) {
        return exporter.findService(clazzName, version);
    }

    @Override
    public int order() {
        return 0;
    }

    @Override
    public void start() {
        this.executorService = new ThreadPoolExecutor(GlobalConstants.EXECUTOR_THREAD_COUNT, GlobalConstants.EXECUTOR_THREAD_COUNT,
                0L, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>());
    }

    @Override
    public boolean isStarted() {
        return executorService != null;
    }
}
