package com.seong.playground.lock.aop;

import com.seong.playground.lock.repository.CacheRepository;
import com.seong.playground.lock.service.JoinEventCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class DistributedLockAspect {

    private final CacheRepository cacheRepository;

    @Around("@annotation(com.seong.playground.lock.aop.DistributedLock)")
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

        while (!cacheRepository.lock(eventId)) {
            Thread.sleep(100);
            log.info("분산락 획득 실패. 재시도 수행.");
        }
        try {
            return joinPoint.proceed();
        } finally {
            cacheRepository.unlock(eventId);
        }

    }
}
