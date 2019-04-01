package com.hquery.hrpc.init;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.Map;

/**
 * @author hquery
 * 2019/3/30 19:27
 * @email hquery@163.com
 */
@Slf4j
@Component
@Order(Integer.MIN_VALUE)
@Lazy(value = false)
public class InitLifeCycle implements ApplicationContextAware, DisposableBean {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        start();
    }


    public void start() {
        ApplicationContext applicationContext = this.applicationContext;
        Map<String, AbstractServerLifeCycle> beansOfType = applicationContext.getBeansOfType(AbstractServerLifeCycle.class);
        if (beansOfType == null || beansOfType.isEmpty()) {
            return;
        }
        AbstractServerLifeCycle.setApplicationContext(this.applicationContext);
        beansOfType.entrySet()
                .stream()
                .sorted(new Comparator<Map.Entry<String, AbstractServerLifeCycle>>() {
                    @Override
                    public int compare(Map.Entry<String, AbstractServerLifeCycle> o1, Map.Entry<String, AbstractServerLifeCycle> o2) {
                        return Integer.compare(o1.getValue().order(), o2.getValue().order());
                    }
                })
                .forEach(serverLifeCycleEntry -> {
                    String key = serverLifeCycleEntry.getKey();
                    ServerLifeCycle value = serverLifeCycleEntry.getValue();
                    try {
                        value.start();
                    } catch (Throwable t) {
                        log.error("初始化类BeanName【{}】出现异常", key, t);
                    }
                });
    }

    @Override
    public void destroy() throws Exception {
        Map<String, AbstractServerLifeCycle> beansOfType = applicationContext.getBeansOfType(AbstractServerLifeCycle.class);
        beansOfType.entrySet()
                .stream()
                .sorted(new Comparator<Map.Entry<String, AbstractServerLifeCycle>>() {
                    @Override
                    public int compare(Map.Entry<String, AbstractServerLifeCycle> o1, Map.Entry<String, AbstractServerLifeCycle> o2) {
                        return Integer.compare(o1.getValue().order(), o2.getValue().order());
                    }
                })
                .forEach(serverLifeCycleEntry -> {
                    String key = serverLifeCycleEntry.getKey();
                    ServerLifeCycle value = serverLifeCycleEntry.getValue();
                    try {
                        System.out.println(key);
                    } catch (Throwable t) {
                        log.error("初始化类BeanName【{}】出现异常", key, t);
                    }
                });
    }
}
