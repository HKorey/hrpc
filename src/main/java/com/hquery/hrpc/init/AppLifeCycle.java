package com.hquery.hrpc.init;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author hquery
 * 2019/3/30 19:27
 * @email hquery@163.com
 */
@Slf4j
@Component
@Order(Integer.MIN_VALUE)
@Lazy(value = false)
public class AppLifeCycle implements ApplicationContextAware, DisposableBean {

    private List<AbstractServerLifeCycle> lifeCycles;

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        init();
        start();
    }

    private void init() {
        ApplicationContext applicationContext = this.applicationContext;
        AbstractServerLifeCycle.setApplicationContext(this.applicationContext);
        Map<String, AbstractServerLifeCycle> beansOfType = applicationContext.getBeansOfType(AbstractServerLifeCycle.class);
        if (beansOfType == null || beansOfType.isEmpty()) {
            return;
        }
        lifeCycles = beansOfType.entrySet()
                .stream()
                .sorted(new Comparator<Map.Entry<String, AbstractServerLifeCycle>>() {
                    @Override
                    public int compare(Map.Entry<String, AbstractServerLifeCycle> o1, Map.Entry<String, AbstractServerLifeCycle> o2) {
                        return Integer.compare(o1.getValue().order(), o2.getValue().order());
                    }
                })
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    public void start() {
        lifeCycles.forEach(serverLifeCycle -> {
            try {
                serverLifeCycle.start();
            } catch (Throwable t) {
                log.error("初始化类BeanName【{}】出现异常", serverLifeCycle.getClass().getName(), t);
            }
        });
    }

    @Override
    public void destroy() throws Exception {
        lifeCycles.forEach(serverLifeCycle -> {
            try {
                serverLifeCycle.stop();
            } catch (Throwable t) {
                log.error("执行BeanName【{}】stop()方法出现异常", serverLifeCycle.getClass().getName(), t);
            }
        });
    }
}
