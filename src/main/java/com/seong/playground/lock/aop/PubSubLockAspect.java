package com.seong.playground.lock.aop;

import com.seong.playground.lock.service.JoinEventCommand;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class PubSubLockAspect {

    private final RedissonClient redissonClient;

    @Order
    @Around("@annotation(com.seong.playground.lock.aop.PubSubLock)")
    public Object proceed(ProceedingJoinPoint joinPoint) throws Throwable {
        Long eventId = null;
        for (Object arg : joinPoint.getArgs()) {
            if(arg instanceof JoinEventCommand command) {
                eventId = command.eventId();
            }
        }
        if(eventId == null) {
            throw new IllegalArgumentException();
        }

        RLock lock = redissonClient.getLock(eventId.toString());
        try {
            boolean available = lock.tryLock(3, 1, TimeUnit.SECONDS);
            if(!available) {
                throw new RuntimeException("락을 획득하지 못했습니다.");
            }
            return joinPoint.proceed();
        } finally {
            lock.unlock();
        }
    }
}
