package com.seong.playground.lock.repository;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RedisRepository implements CacheRepository{

    private final RedisTemplate<String,String> redisTemplate;

    @Override
    public Boolean lock(Long key) {
        return redisTemplate
            .opsForValue()
            .setIfAbsent(key.toString(), "lock", Duration.ofMillis(3000));
    }

    @Override
    public Boolean unlock(Long key) {
        return redisTemplate.delete(key.toString());
    }
}
