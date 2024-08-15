package com.seong.playground.lock.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.seong.playground.common.BaseIntegrationTest;
import com.seong.playground.common.Member;
import com.seong.playground.lock.Event;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

class EventServiceTest extends BaseIntegrationTest {

    @Autowired
    EventService eventService;

    @Autowired
    EventFacade eventFacade;

    private List<Member> createMembers(int count) {
        List<Member> members = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            members.add(new Member());
        }
        return members;
    }

    @Nested
    @DisplayName("10명 제한, 25명 신청")
    class Limit5_Apply10 {

        private ExecutorService executor;
        private CountDownLatch latch;
        private List<Member> members;
        private Event event;

        private final int threadPoolSize = 25;
        private final int limitOfPeople = 10;

        @BeforeEach
        void setUp() {
            executor = Executors.newFixedThreadPool(threadPoolSize);
            latch = new CountDownLatch(threadPoolSize);
            members = createMembers(threadPoolSize);
            event = new Event(limitOfPeople);

            persist(members, event);
        }

        private long countEventMembers(Event event) {
            return em.createQuery("select count(em) from EventMember em "
                    + "where event=:event", Long.class)
                .setParameter("event", event)
                .getSingleResult();
        }

        @Test
        @DisplayName("락 사용안함")
        void withOutLock() throws InterruptedException {
            //given
            //when
            for (int i = 0; i < threadPoolSize; i++) {
                int finalI = i;
                executor.submit(() -> {
                    JoinEventCommand command = new JoinEventCommand(
                        members.get(finalI).getMemberId(), event.getEventId());
                    eventService.joinEvent(command);
                });
                latch.countDown();
            }
            latch.await();

            //then
            Long count = countEventMembers(event);
            assertThat(count).isNotEqualTo(limitOfPeople);
        }

        @Test
        @DisplayName("낙관적 락 -> 데드락")
        void optimisticLock() throws InterruptedException {
            //given
            //when
            for (int i = 0; i < threadPoolSize; i++) {
                int finalI = i;
                executor.submit(() -> {
                    JoinEventCommand command = new JoinEventCommand(
                        members.get(finalI).getMemberId(), event.getEventId());
                    try {
                        eventFacade.joinEventWithOptimistic(command);
                    } finally {
                        latch.countDown();
                    }
                });
            }
            latch.await();

            //then
            Long count = countEventMembers(event);
            assertThat(count).isEqualTo(limitOfPeople);
        }

        @Test
        @DisplayName("비관적 락")
        void pessimisticLock() throws InterruptedException {
            //given
            //when
            for (int i = 0; i < threadPoolSize; i++) {
                int finalI = i;
                executor.submit(() -> {
                    JoinEventCommand command = new JoinEventCommand(
                        members.get(finalI).getMemberId(), event.getEventId());
                    try {
                        eventService.joinEventWithPessimistic(command);
                    } finally {
                        latch.countDown();
                    }
                });
            }
            latch.await();

            //then
            Long count = countEventMembers(event);
            assertThat(count).isEqualTo(limitOfPeople);
        }

        @Test
        @DisplayName("분산 락 - 스핀 락")
        void redisLock() throws InterruptedException {
            //given
            //when
            for (int i = 0; i < threadPoolSize; i++) {
                int finalI = i;
                executor.submit(() -> {
                    JoinEventCommand command = new JoinEventCommand(
                        members.get(finalI).getMemberId(), event.getEventId());
                    try {
                        eventFacade.joinEventWithRedis(command);
                    } finally {
                        latch.countDown();
                    }
                });
            }
            latch.await();

            //then
            Long count = countEventMembers(event);
            assertThat(count).isEqualTo(limitOfPeople);
        }

        @Test
        @DisplayName("분산 락 - 스핀 락 AOP")
        void redisLockWithAOP() throws InterruptedException {
            //given
            //when
            for (int i = 0; i < threadPoolSize; i++) {
                int finalI = i;
                executor.submit(() -> {
                    JoinEventCommand command = new JoinEventCommand(
                        members.get(finalI).getMemberId(), event.getEventId());
                    try {
                        eventService.joinEventWithDistributedLock(command);
                    } finally {
                        latch.countDown();
                    }
                });
            }
            latch.await();

            //then
            Long count = countEventMembers(event);
            assertThat(count).isEqualTo(limitOfPeople);
        }

        @Test
        @DisplayName("분산 락 - Pub/Sub")
        void pubSubLock() throws InterruptedException {
            //given
            //when
            for (int i = 0; i < threadPoolSize; i++) {
                int finalI = i;
                executor.submit(() -> {
                    JoinEventCommand command = new JoinEventCommand(
                        members.get(finalI).getMemberId(), event.getEventId());
                    try {
                        eventFacade.joinEventWithPubSub(command);
                    } finally {
                        latch.countDown();
                    }
                });
            }
            latch.await();

            //then
            Long count = countEventMembers(event);
            assertThat(count).isEqualTo(limitOfPeople);
        }

        @Test
        @DisplayName("분산 락 - Pub/Sub AOP")
        void pubSubLockWithAop() throws InterruptedException {
            //given
            //when
            for (int i = 0; i < threadPoolSize; i++) {
                int finalI = i;
                executor.submit(() -> {
                    JoinEventCommand command = new JoinEventCommand(
                        members.get(finalI).getMemberId(), event.getEventId());
                    try {
                        eventService.joinEventWithPubSubLock(command);
                    } finally {
                        latch.countDown();
                    }
                });
            }
            latch.await();

            //then
            Long count = countEventMembers(event);
            assertThat(count).isEqualTo(limitOfPeople);
        }
    }

}