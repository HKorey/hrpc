package com.hquery.hrpc.export;

import com.hquery.hrpc.annotation.RegisterRpcServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

/**
 * @author hquery.huang
 * 2019/3/25 17:33:17
 */
@Slf4j
@Component
public class ExportServerScanner implements BeanPostProcessor {

    @Nullable
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        log.info("BeanPostProcessor, beanName{}, beanName ： {}", beanClass.getName(), beanName);
        RegisterRpcServer annotation = beanClass.getAnnotation(RegisterRpcServer.class);
        if (annotation == null) {
            return bean;
        }
        log.info("BeanPostProcessor, beanName{}, beanName", beanClass.getName(), beanName);
        beanClass.getSuperclass();
        return bean;
    }

}
