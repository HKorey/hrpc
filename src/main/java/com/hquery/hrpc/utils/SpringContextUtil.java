package com.hquery.hrpc.utils;

import com.google.common.base.Optional;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * @author hquery.huang
 * 2018/5/3 17:00
 */
@Slf4j
@Component
@Lazy(value = false)
@Order(Integer.MIN_VALUE)
public class SpringContextUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    /**
     * 缓存常用引用
     *
     * @author hquery
     * 2019/4/4 11:29:34
     */
    private static final Cache<String, Optional<Object>> BEAN_NAME_CACHE = CacheBuilder.newBuilder().build();

    /**
     * 缓存常用引用
     *
     * @author hquery
     * 2019/4/4 11:29:34
     */
    private static final Cache<Class<?>, Optional<Object>> BEAN_TYPE_CACHE = CacheBuilder.newBuilder().build();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextUtil.applicationContext = applicationContext;
    }

    public static <T> T getBean(String name) {
        try {
            Optional<Object> optional = BEAN_NAME_CACHE.get(name, () -> {
                Object bean = applicationContext.getBean(name);
                if (bean == null) {
                    Optional.fromNullable(null);
                }
                return Optional.of(bean);
            });
            return optional.isPresent() ? (T) optional.get() : null;
        } catch (ExecutionException e) {
            log.error("[E3][获取Bean: {} 出错]", name, e);
            return null;
        }
    }

    public static <T> T getBean(Class<T> requiredType) {
        try {
            Optional optional = BEAN_TYPE_CACHE.get(requiredType, () -> {
                T bean = applicationContext.getBean(requiredType);
                if (bean == null) {
                    Optional.fromNullable(null);
                }
                return Optional.of(bean);
            });
            return optional.isPresent() ? (T) optional.get() : null;
        } catch (ExecutionException e) {
            log.error("[E3][获取BeanType: {} 出错]", requiredType.getName(), e);
            return null;
        }
    }

    public static <T> Map<String, T> getBeansOfType(Class<T> type) {
        return applicationContext.getBeansOfType(type);
    }
}
