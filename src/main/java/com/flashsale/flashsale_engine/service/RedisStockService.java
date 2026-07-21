package com.flashsale.flashsale_engine.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisStockService {

    // RedisTemplate is spring's class for redis operations ; here keys and values both are string
    private final RedisTemplate<String, String> redisTemplate;

    // name convention for redis keys (Full key becomes flash:stock:1 for sneaker id 1)
    private static final String STOCK_KEY_PREFIX = "flash:stock:";

    // Initializes stock in Redis when sale starts
    public void initializeStock(Long sneakerId, int quantity) {
        String key = STOCK_KEY_PREFIX + sneakerId;
        redisTemplate.opsForValue().set(key, String.valueOf(quantity));
        System.out.println("Redis stock initialized for sneaker " + sneakerId + " = " + quantity);
    }

    // Atomically decrement stock thus it returns remaining stock after decrement
    public Long decrementStock(Long sneakerId) {
        String key = STOCK_KEY_PREFIX + sneakerId;
        return redisTemplate.opsForValue().decrement(key);  // DECR command me translate hoga ye
    }

    // Restore stock if order creation fails after decrement
    public void incrementStock(Long sneakerId) {
        String key = STOCK_KEY_PREFIX + sneakerId;
        redisTemplate.opsForValue().increment(key);  // INCR command me translate hoga ye
    }

    // Get current stock from Redis
    public Long getStock(Long sneakerId) {
        String key = STOCK_KEY_PREFIX + sneakerId;
        String value = redisTemplate.opsForValue().get(key);
        return value != null ? Long.parseLong(value) : null;
    }

    // Check if stock is initialized in Redis
    public boolean isStockInitialized(Long sneakerId) {
        String key = STOCK_KEY_PREFIX + sneakerId;
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }
}