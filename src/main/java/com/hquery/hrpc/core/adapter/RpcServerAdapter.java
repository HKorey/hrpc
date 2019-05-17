package com.hquery.hrpc.core.adapter;

import com.hquery.hrpc.core.discover.ServiceDiscovery;
import com.hquery.hrpc.core.proxy.RpcProxy;
import com.hquery.hrpc.utils.RpcConfUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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
//        AbstractRpcRoute route = AbstractRpcRoute.getInstance(routeType);

        for (Map.Entry<String, String> entry : RpcConfUtil.getConfCache().entrySet()) {
            log.info("Server name:{}, class:{}", entry.getKey(), entry.getValue());
//            RpcConnector connector = new NettyRpcConnector("", 1234);
            RpcProxy proxy = new RpcProxy();
//            RpcClusterProxy proxy = new RpcClusterProxy(route);
//            Class<?> serviceClazz;
            try {
                Class<?> service = Class.forName(entry.getValue());
                if (!ServiceDiscovery.putService(service, new ArrayList<>())) {
                    log.error("配置文件出现重复类配置 {}", entry.getValue());
                    continue;
                }
                beanFactory.registerSingleton(entry.getKey(), new RpcServiceFactoryBean(proxy, service));
//                serviceClazz = service;
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Can not resolved class " + entry.getValue(), e);
            }
        }
    }

}
