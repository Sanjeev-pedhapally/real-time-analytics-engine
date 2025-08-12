package com.faang.analytics.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

public class RedisService {
    private static final Logger logger = LoggerFactory.getLogger(RedisService.class);
    private final JedisPool jedisPool;

    public RedisService(String host, int port, int maxTotal) {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxTotal);
        config.setTestOnBorrow(true);
        config.setTestOnReturn(true);
        this.jedisPool = new JedisPool(config, host, port);
    }

    public String get(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            String value = jedis.get(key);
            return value != null ? value : "";
        } catch (Exception e) {
            logger.error("Redis get error: {}", e.getMessage());
            return "";
        }
    }

    public void publish(String channel, String message) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.publish(channel, message);
        } catch (Exception e) {
            logger.error("Redis publish error: {}", e.getMessage());
        }
    }

    public void subscribe(String channel, JedisPubSub listener) {
        new Thread(() -> {
            try (Jedis jedis = jedisPool.getResource()) {
                jedis.subscribe(listener, channel);
            } catch (Exception e) {
                logger.error("Redis subscribe error: {}", e.getMessage());
            }
        }).start();
    }

    public void close() {
        jedisPool.close();
    }
}
