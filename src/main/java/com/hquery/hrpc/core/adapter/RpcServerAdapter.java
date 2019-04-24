package com.hquery.hrpc.core.adapter;

import com.hquery.hrpc.constants.GlobalConstants;
import com.hquery.hrpc.core.connector.NettyRpcConnector;
import com.hquery.hrpc.core.connector.RpcConnector;
import com.hquery.hrpc.core.discover.ServiceDiscovery;
import com.hquery.hrpc.core.exception.RpcException;
import com.hquery.hrpc.core.proxy.RpcClusterProxy;
import com.hquery.hrpc.core.proxy.RpcProxy;
import com.hquery.hrpc.core.registry.DefaultRegistry;
import com.hquery.hrpc.core.route.AbstractRpcRoute;
import com.hquery.hrpc.utils.SpringContextUtil;
import jodd.util.CollectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author hquery.huang
 * 2019/4/2 12:08:36
 */
@Slf4j
@Component
public class RpcServerAdapter implements BeanDefinitionRegistryPostProcessor {

    @Value("${hrpc.server.registry.protocol.client:zookeeper}")
    private String registryProtocol;

    @Value("${hrpc.route.type:random}")
    private String routeType = "random";

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
        AbstractRpcRoute route = AbstractRpcRoute.getInstance(routeType);

        for (Map.Entry<String, String> entry : initRpcServerConf().entrySet()) {
            log.info("Server name:{}, class:{}", entry.getKey(), entry.getValue());
            // TODO
            RpcConnector connector = new NettyRpcConnector("", 1234);
            RpcProxy proxy = new RpcProxy(connector);
//            RpcClusterProxy proxy = new RpcClusterProxy(route);
            Class<?> serviceClazz;
            try {
                Class<?> service = Class.forName(entry.getValue());
                if (!ServiceDiscovery.putService(service, new ArrayList<>())) {
                    log.error("配置文件出现重复类配置 {}", entry.getValue());
                    continue;
                }
                beanFactory.registerSingleton(entry.getKey(), new RpcServiceFactoryBean(proxy, service));
                serviceClazz = service;
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Can not resolved class " + entry.getValue(), e);
            }
            try {
                DefaultRegistry registry = SpringContextUtil.getBean(registryProtocol + GlobalConstants.REGISTRY_PROTOCOL_SUFFIX);
                if (registry == null) {
                    throw new RpcException("未找到注册协议，请配置hrpc.server.registry.protocol.client");
                }
                registry.registerClient(serviceClazz, GlobalConstants.DEFAULT_LOCAL_HOST);
            } catch (Exception e) {
//                log.error("Connect server[{}][{}:{}] error", entry.getValue(), address, port, e);
            }
        }
    }

    /**
     * 初始化读取RPC配置
     *
     * @param
     * @return boolean
     * @author hquery
     * 2019/4/2 16:07:19
     */
    private Map<String, String> initRpcServerConf() {
        Properties properties = new Properties();
        InputStream resourceAsStream = getClass().getResourceAsStream("/conf/application-rpc-server-conf.properties");
        try {
            if (resourceAsStream == null || resourceAsStream.available() == -1) {
                log.info("application-rpc-server-conf.properties not found");
                return Collections.EMPTY_MAP;
            }
            properties.load(resourceAsStream);
        } catch (IOException e) {
            throw new RuntimeException("load file 【/conf/application-rpc-server-conf.properties】 error", e);
        }
        return properties.stringPropertyNames()
                .stream()
                .distinct()
                .collect(Collectors.toMap(pk -> fetchBeanName((String) pk),
                        p -> properties.getProperty(p).trim(),
                        (u, v) -> {
                            throw new IllegalStateException(String.format("Duplicate key %s", u));
                        },
                        TreeMap::new));
    }

    /**
     * 获取注册类名
     *
     * @param pk
     * @return java.lang.String
     * @author hquery
     * 2019/4/4 15:05:22
     */
    private String fetchBeanName(String pk) {
        String key = pk.trim();
        return key.substring(key.lastIndexOf("[") + 1, key.length() - 1);
    }

}
