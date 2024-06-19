package com.seong.playground.lock.service;

import com.seong.playground.lock.repository.CacheRepository;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventFacade {

    private final EventService eventService;
    private final RedissonClient redissonClient;
    private final CacheRepository cacheRepository;

    public Long joinEventWithOptimistic(JoinEventCommand command) {
        while (true) {
            try {
                return eventService.joinEventWithOptimistic(command);
            } catch (ObjectOptimisticLockingFailureException ignored) {
                log.info("낙과적 락 충돌 발생. 재시도.");
            } catch (PessimisticLockingFailureException e) {
                log.info("낙관적 락 데드락 발생. 재시도.");
            }
        }
    }

    public Long joinEventWithRedis(JoinEventCommand command) {
        try {
            while (!cacheRepository.lock(command.eventId())) {
                Thread.sleep(100);
                log.info("스핀락 획득 실패. 재시도.");
            }
            return eventService.joinEvent(command);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            cacheRepository.unlock(command.eventId());
        }
    }

    public Long joinEventWithPubSub(JoinEventCommand command) {
        RLock lock = redissonClient.getLock(command.eventId().toString());

        try {
            boolean available = lock.tryLock(3, 1, TimeUnit.SECONDS);
            if(!available) {
                throw new RuntimeException("락 획득 실패");
            }
            return eventService.joinEvent(command);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }
}
