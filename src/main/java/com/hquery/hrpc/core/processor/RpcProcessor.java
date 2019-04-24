package com.hquery.hrpc.core.processor;

import com.hquery.hrpc.constants.GlobalConstants;
import com.hquery.hrpc.core.server.Exporter;
import com.hquery.hrpc.init.AbstractServerLifeCycle;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

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
                0L, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>(), new ThreadFactory() {
            private AtomicLong count = new AtomicLong();

            @Override
            public Thread newThread(Runnable r) {
                return new Thread("HRPC-processing-" + count.incrementAndGet());
            }
        });
    }

    @Override
    public boolean isStarted() {
        return executorService != null;
    }
}
