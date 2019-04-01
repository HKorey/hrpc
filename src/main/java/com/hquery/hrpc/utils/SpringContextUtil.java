package com.hquery.hrpc.utils;

import com.hquery.hrpc.init.AbstractServerLifeCycle;
import com.hquery.hrpc.init.ServerLifeCycle;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * @author hquery.huang
 * 2018/5/3 17:00
 */
@Component
public class SpringContextUtil extends AbstractServerLifeCycle {

    public static <T> T getBean(String name) {
        return (T) applicationContext.getBean(name);
    }

    public static <T> T getBean(Class<T> requiredType) {
        return applicationContext.getBean(requiredType);
    }

    @Override
    public boolean isStarted() {
        return applicationContext != null;
    }

    @Override
    public int order() {
        return -1;
    }
}
