package com.seong.playground.testdouble.stub;

import com.seong.playground.common.Member;
import com.seong.playground.common.MemberRepository;
import com.seong.playground.testdouble.fixture.MemberFixture;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class StubMemberRepository implements MemberRepository {

    private final Map<Long, Member> database = new HashMap<>();

    public void stub(Member member) {
        database.put(getNextId(), member);
    }

    private Long getNextId() {
        return (long) (database.size() + 1);
    }

    @Override
    public Optional<Member> findById(Long memberId) {
        if(database.isEmpty()) {
            return Optional.of(MemberFixture.member());
        }
        return database.values().stream().findFirst();
    }

    @Override
    public Long save(Member member) {
        stub(member);
        return (long) database.size();
    }
}
