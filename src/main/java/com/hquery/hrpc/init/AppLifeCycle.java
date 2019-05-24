package com.hquery.hrpc.init;

import com.hquery.hrpc.utils.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
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
@DependsOn("springContextUtil")
public class AppLifeCycle implements DisposableBean {

    private List<AbstractServerLifeCycle> lifeCycles;

    @PostConstruct
    public void init() {
        initAppLifeCycle();
        start();
    }

    private void initAppLifeCycle() {
        Map<String, AbstractServerLifeCycle> beansOfType = SpringContextUtil.getBeansOfType(AbstractServerLifeCycle.class);
        if (beansOfType == null || beansOfType.isEmpty()) {
            return;
        }
        lifeCycles = beansOfType.entrySet()
                .stream()
                .sorted(Comparator.comparingInt(o -> o.getValue().order()))
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
