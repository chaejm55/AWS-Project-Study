package com.example.awsproject.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate<String, String> redisTemplate;

    // LPUSH
    public void lPush(String key, String value) {
        redisTemplate.opsForList().leftPush(key, value);
    }

    // LRANGE
    public List<String> lRange(String key, int start, int stop) {
        return redisTemplate.opsForList().range(key, start, stop);
    }

    public void deleteKey(String key) {
        redisTemplate.delete(key);
    }
}
