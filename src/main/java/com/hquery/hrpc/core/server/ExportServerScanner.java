package com.hquery.hrpc.core.server;

import com.hquery.hrpc.annotation.RegisterRpcServer;
import com.hquery.hrpc.core.server.RpcServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 暴露Rpc服务
 *
 * @author hquery.huang
 * 2019/3/25 17:33:17
 */
@Slf4j
@Component
public class ExportServerScanner implements BeanPostProcessor {

    @Resource
    private RpcServer rpcServer;

    @Nullable
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        RegisterRpcServer annotation = beanClass.getAnnotation(RegisterRpcServer.class);
        if (annotation == null) {
            return bean;
        }
        if (!annotation.exportInterface().isAssignableFrom(beanClass)) {
            log.error("RPC注册失败-注册实例【{}】未实现接口【{}】", beanName, annotation.exportInterface().getName());
            return bean;
        }
        log.info("Export RPC server, beanName:{}", beanName);
        try {
            rpcServer.export(annotation.exportInterface(), bean);
        } catch (Throwable t) {
            log.error("Export RPC server error, beanName:{}", beanName, t);
        }
        return bean;
    }

}
