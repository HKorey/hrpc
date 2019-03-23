package com.hquery.hrpc.core;

import com.hquery.hrpc.constants.GlobalConstants;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author hquery.huang
 * 2019/3/23 15:49:43
 */
public class RpcProcessor {

    private ExecutorService executorService;

    private Exporter exporter;

    public RpcProcessor(Exporter exporter) {
        this.executorService = new ThreadPoolExecutor(GlobalConstants.EXECUTOR_THREAD_COUNT, GlobalConstants.EXECUTOR_THREAD_COUNT,
                0L, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>());
        this.exporter = exporter;
    }

    public void submit(Runnable runnable) {
        executorService.submit(runnable);
    }

    public Object findService(String clazzName) {
        return findService(clazzName, null);
    }

    public Object findService(String clazzName, String version) {
        return exporter.findService(clazzName, version);
    }

}
