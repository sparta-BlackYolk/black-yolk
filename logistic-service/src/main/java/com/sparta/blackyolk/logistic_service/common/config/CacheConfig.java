package com.sparta.blackyolk.logistic_service.common.config;

import java.time.Duration;
import java.util.Map;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.CacheKeyPrefix;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public RedisCacheManager cacheManager(
        RedisConnectionFactory redisConnectionFactory
    ) {
        RedisCacheConfiguration configuration = RedisCacheConfiguration
            .defaultCacheConfig()
            .disableCachingNullValues()
            .computePrefixWith(CacheKeyPrefix.simple())
            .serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(
                    new GenericJackson2JsonRedisSerializer()
                )
            );

        // 적용 하고 싶은 캐싱을 선언 합니다.
        Map<String, RedisCacheConfiguration> cacheConfigurations = Map.of(
            "hub_page_cache", configuration.entryTtl(Duration.ofMinutes(60)), // 60분 TTL
            "hub_cache", configuration.entryTtl(Duration.ofMinutes(90)), // 90분 TTL
            "hub_route_page_cache", configuration.entryTtl(Duration.ofMinutes(60)), // 60분 TTL
            "hub_route_cache", configuration.entryTtl(Duration.ofMinutes(60)), // 60분 TTL
            "hub_route_path_cache", configuration.entryTtl(Duration.ofMinutes(10))  // 10분 TTL
        );

        return RedisCacheManager
            .builder(redisConnectionFactory)
            .cacheDefaults(configuration)
            .withInitialCacheConfigurations(cacheConfigurations)
            .build();
    }
}
