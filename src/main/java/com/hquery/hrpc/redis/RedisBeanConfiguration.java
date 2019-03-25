package com.hquery.hrpc.redis;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author hquery.huang
 * 2019/3/25 16:26:09
 */
//@Configuration
public class RedisBeanConfiguration {

    @Value("${redisson.cluster.address}")
    private String address;

    @Value("${redisson.cluster.password}")
    private String password;

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
//        config.useSentinelServers()
//                .addSentinelAddress("127.0.0.1:6379", "127.0.0.1:6369", "127.0.0.1:6389")
//                .setMasterName("masterName")
//                .setPassword("passwd")
//                .setDatabase(0);
        config.useClusterServers()
                .addNodeAddress(address.split(","))
                .setPassword(password);
        return Redisson.create(config);
    }

}
