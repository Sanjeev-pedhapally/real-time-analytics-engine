package com.faang.analytics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class CacheManager {
    private static final Logger logger = LoggerFactory.getLogger(CacheManager.class);
    private final JedisPool jedisPool;

    public CacheManager(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    // Cache-aside pattern: get from cache, else load and cache
    public String getOrLoad(String key, java.util.function.Supplier<String> loader, int ttlSeconds) {
        try (Jedis jedis = jedisPool.getResource()) {
            String value = jedis.get(key);
            if (value != null) {
                return value;
            }
            value = loader.get();
            if (value != null) {
                jedis.setex(key, ttlSeconds, value);
            }
            return value;
        } catch (Exception e) {
            logger.error("CacheManager error: {}", e.getMessage());
            return loader.get();
        }
    }

    public void invalidate(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.del(key);
        } catch (Exception e) {
            logger.error("CacheManager invalidate error: {}", e.getMessage());
        }
    }
}
