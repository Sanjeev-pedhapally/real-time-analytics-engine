package com.faang.analytics.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Jedis;

@Service
public class RedisService {
    private static final Logger logger = LoggerFactory.getLogger(RedisService.class);
    private final JedisPool jedisPool;

    public RedisService(
            @Value("${REDIS_HOST:redis}") String host,
            @Value("${REDIS_PORT:6379}") int port,
            @Value("${REDIS_MAX_TOTAL:10}") int maxTotal) {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxTotal);
        config.setTestOnBorrow(true);
        config.setTestOnReturn(true);
        this.jedisPool = new JedisPool(config, host, port);
    }

    public String get(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.get(key);
        } catch (Exception e) {
            logger.error("Redis get error: {}", e.getMessage());
            return null;
        }
    }

    public void set(String key, String value) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.set(key, value);
        } catch (Exception e) {
            logger.error("Redis set error: {}", e.getMessage());
        }
    }

    public void close() {
        jedisPool.close();
    }
}
