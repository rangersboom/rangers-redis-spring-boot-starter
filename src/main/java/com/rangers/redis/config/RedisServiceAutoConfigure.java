package com.rangers.redis.config;

import com.alibaba.fastjson.parser.ParserConfig;
import com.rangers.redis.RedisService;
import com.rangers.redis.serializer.FastJsonSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Description——自动配置类
 * Created By Rangers At 2023/6/2
 **/
@Configuration
@ConditionalOnClass(RedisService.class)
public class RedisServiceAutoConfigure {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * key 的序列化器
     */
    private final StringRedisSerializer keyRedisSerializer = new StringRedisSerializer();

    /**
     * value 的序列化器
     */
    private final FastJsonSerializer<Object> valueRedisSerializer = new FastJsonSerializer<>(Object.class);

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        // RedisCacheWriter
        RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory);
        // RedisCacheConfiguration - 值的序列化方式
        RedisSerializationContext.SerializationPair<Object> serializationPair = RedisSerializationContext.SerializationPair.fromSerializer(valueRedisSerializer);
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig().serializeValuesWith(serializationPair);

        return new RedisCacheManager(redisCacheWriter, redisCacheConfiguration);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        // 全局开启AutoType
        ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
        // 配置连接工厂
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        // 键序列化-StringRedisSerializer
        redisTemplate.setKeySerializer(keyRedisSerializer);
        // 值序列化-RedisFastJsonSerializer
        redisTemplate.setValueSerializer(valueRedisSerializer);
        redisTemplate.setHashValueSerializer(valueRedisSerializer);
        redisTemplate.setHashKeySerializer(keyRedisSerializer);
        return redisTemplate;
    }

}
