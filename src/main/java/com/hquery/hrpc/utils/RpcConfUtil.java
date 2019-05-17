package com.hquery.hrpc.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * @author hquery.huang
 * 2019/5/17 16:42:29
 */
@Slf4j
public class RpcConfUtil {

    private static TreeMap<String, String> CONF_CACHE = new TreeMap<>();

    static {
        Properties properties = new Properties();
        InputStream resourceAsStream = RpcConfUtil.class.getResourceAsStream("/conf/application-rpc-server-conf.properties");
        try {
            if (resourceAsStream == null || resourceAsStream.available() == -1) {
                log.info("application-rpc-server-conf.properties not found");
            } else {
                properties.load(resourceAsStream);
                CONF_CACHE = properties.stringPropertyNames()
                        .stream()
                        .distinct()
                        .collect(Collectors.toMap(pk -> fetchBeanName((String) pk),
                                p -> properties.getProperty(p.toString()).trim(),
                                (u, v) -> {
                                    throw new IllegalStateException(String.format("Duplicate key %s", u));
                                },
                                TreeMap::new));
            }
        } catch (Exception e) {
            throw new RuntimeException("load file 【/conf/application-rpc-server-conf.properties】 fail", e);
        }
    }

    /**
     * 读取RPC配置
     *
     * @param
     * @return boolean
     * @author hquery
     * 2019/4/2 16:07:19
     */
    public static TreeMap<String, String> getConfCache() {
        return CONF_CACHE;
    }

    /**
     * 获取注册类名
     *
     * @param pk
     * @return java.lang.String
     * @author hquery
     * 2019/4/4 15:05:22
     */
    private static String fetchBeanName(String pk) {
        String key = pk.trim();
        return key.substring(key.lastIndexOf("[") + 1, key.length() - 1);
    }

}
