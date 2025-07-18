package com.sky.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Slf4j
@Configuration
public class RedisConfiguration {

    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        log.info("开始创建RedisTemplate模板对象");
        RedisTemplate redisTemplate = new RedisTemplate();
        //设置工厂连接对象
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        //设置序列化起
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        return redisTemplate;
    }
}
