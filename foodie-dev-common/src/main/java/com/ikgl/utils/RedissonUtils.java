package com.ikgl.utils;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.io.IOException;

public class RedissonUtils {

    public static RedissonClient getRedissonClient() throws IOException {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379").setPassword("imooc");
        RedissonClient client = Redisson.create(config);
        return client;
    }
}
