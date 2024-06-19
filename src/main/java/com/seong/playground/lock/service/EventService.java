package com.seong.playground.lock.service;

import com.seong.playground.common.Member;
import com.seong.playground.common.MemberRepository;
import com.seong.playground.lock.Event;
import com.seong.playground.lock.EventMember;
import com.seong.playground.lock.repository.CacheRepository;
import com.seong.playground.lock.repository.EventMemberRepository;
import com.seong.playground.lock.repository.EventRepository;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final MemberRepository memberRepository;
    private final EventMemberRepository eventMemberRepository;

    public Long joinEvent(JoinEventCommand command) {
        Event event = eventRepository.findById(command.eventId())
            .orElseThrow(NoSuchElementException::new);
        Member member = memberRepository.findById(command.memberId())
            .orElseThrow(NoSuchElementException::new);
        long joinedMember = eventMemberRepository.countByEvent(event);
        EventMember newEventMember = event.join(member, joinedMember);
        eventMemberRepository.save(newEventMember);
        return newEventMember.getEventMemberId();
    }

    public Long joinEventWithOptimistic(JoinEventCommand command) {
        Event event = eventRepository.findByIdOptimistic(command.eventId())
            .orElseThrow(NoSuchElementException::new);
        Member member = memberRepository.findById(command.memberId())
            .orElseThrow(NoSuchElementException::new);
        long joinedMember = eventMemberRepository.countByEvent(event);
        event.validate(joinedMember);
        EventMember newEventMember = new EventMember(event, member);
        eventMemberRepository.save(newEventMember);
        return newEventMember.getEventMemberId();
    }

    public Long joinEventWithPessimistic(JoinEventCommand command) {
        Event event = eventRepository.findByIdPessimistic(command.eventId())
            .orElseThrow(NoSuchElementException::new);
        Member member = memberRepository.findById(command.memberId())
            .orElseThrow(NoSuchElementException::new);
        long joinedMember = eventMemberRepository.countByEvent(event);
        event.validate(joinedMember);
        EventMember newEventMember = new EventMember(event, member);
        eventMemberRepository.save(newEventMember);
        return newEventMember.getEventMemberId();
    }
}
