package com.hquery.hrpc.core.adapter;

import com.hquery.hrpc.core.discover.ServiceDiscovery;
import com.hquery.hrpc.core.proxy.RpcClusterProxy;
import com.hquery.hrpc.core.proxy.RpcProxy;
import com.hquery.hrpc.core.server.RemoteServerWrapper;
import com.hquery.hrpc.utils.RpcConfUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author hquery.huang
 * 2019/4/2 12:08:36
 */
@Slf4j
@Component
public class RpcServerAdapter implements BeanDefinitionRegistryPostProcessor {

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
    }

    /**
     * 构建代理对象，加入容器
     *
     * @param beanFactory
     * @return void
     * @author hquery
     * 2019/4/2 15:16:38
     */
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        for (Map.Entry<String, String> entry : RpcConfUtil.getConfCache().entrySet()) {
            log.info("Server name:{}, class:{}", entry.getKey(), entry.getValue());
            RpcProxy proxy = new RpcClusterProxy();
            try {
                Class<?> service = Class.forName(entry.getValue());
                RemoteServerWrapper wrapper = new RemoteServerWrapper(service);
                if (!ServiceDiscovery.putService(service, wrapper)) {
                    log.error("配置文件出现重复类配置 {}", entry.getValue());
                    continue;
                }
                beanFactory.registerSingleton(entry.getKey(), new RpcServiceFactoryBean(proxy, service));
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Can not resolved class " + entry.getValue(), e);
            }
        }
    }

}
