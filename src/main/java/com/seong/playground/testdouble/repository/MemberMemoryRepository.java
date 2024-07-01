package com.seong.playground.testdouble.repository;

import com.seong.playground.common.Member;
import com.seong.playground.common.MemberRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MemberMemoryRepository implements MemberRepository {

    private final Map<Long, Member> database = new HashMap<>();

    @Override
    public Optional<Member> findById(Long memberId) {
        return Optional.ofNullable(database.get(memberId));
    }

    @Override
    public Long save(Member member) {
        database.put(getNextId(), member);
        return (long) database.size();
    }

    private Long getNextId() {
        return (long) (database.size() + 1);
    }
}
