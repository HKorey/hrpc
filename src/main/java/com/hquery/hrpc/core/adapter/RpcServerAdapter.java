package com.hquery.hrpc.core.adapter;

import com.hquery.hrpc.core.client.RpcClient;
import com.hquery.hrpc.core.connector.NettyRpcConnector;
import com.hquery.hrpc.core.connector.RpcConnector;
import com.hquery.hrpc.core.proxy.RpcProxy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

/**
 * @author hquery.huang
 * 2019/4/2 12:08:36
 */
@Slf4j
@Component
public class RpcServerAdapter implements BeanDefinitionRegistryPostProcessor {

    /**
     * 客户端配置RPC引用
     */
    private Map<String, String> interfaces = new HashMap<>();

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
        if (!initRpcServerConf()) {
            return;
        }
        interfaces.entrySet().forEach(entry -> {
            // TODO 查看ZK是否暴露了此服务，并获取服务地址+端口
            log.info("Server name:{}, class:{}", entry.getKey(), entry.getValue());
            String address = "127.0.0.1";
            int port = 52710;
            try {
                RpcConnector connector = new NettyRpcConnector(address, port);
                RpcProxy proxy = new RpcProxy(connector);
//                RpcClient rpcClient = RpcClient.builder()
//                        .proxy(proxy)
//                        .rpcConnector(connector).build();
                connector.start();
                Class<?> serviceClazz = Class.forName(entry.getValue());
                beanFactory.registerSingleton(entry.getKey(), new RpcServiceFactoryBean<>(proxy, serviceClazz));
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Can not resolved class " + entry.getValue(), e);
            } catch (Exception e) {
//                throw new RuntimeException("Connect server[" + address + ":" + port + "] error", e);
                log.error("Connect server[{}:{}] error", address, port);
            }
        });
    }

    /**
     * 初始化读取RPC配置
     *
     * @param
     * @return boolean
     * @author hquery
     * 2019/4/2 16:07:19
     */
    private boolean initRpcServerConf() {
        Properties properties = new Properties();
        InputStream resourceAsStream = getClass().getResourceAsStream("/conf/application-rpc-server-conf.properties");
        try {
            if (resourceAsStream == null || resourceAsStream.available() == -1) {
                log.info("application-rpc-server-conf.properties not found");
                return false;
            }
            properties.load(resourceAsStream);
        } catch (IOException e) {
            throw new RuntimeException("Load /conf/application-rpc-server-conf.properties error", e);
        }
        Iterator<String> it = properties.stringPropertyNames().iterator();
        while (it.hasNext()) {
            String key = it.next();
            log.info("key : {}:{}", key, properties.get(key).toString());
            interfaces.put(key.substring(key.lastIndexOf("[") + 1, key.length() - 1), properties.get(key).toString());
        }
        return true;
    }
}
