package com.seong.playground.lock.repository;

import com.seong.playground.lock.Event;
import com.seong.playground.lock.EventMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventMemberRepository extends JpaRepository<EventMember, Long> {

    long countByEvent(Event event);
}
