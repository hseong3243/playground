package com.seong.playground.testdouble.fixture;

import com.seong.playground.common.Member;

public class MemberFixture {

    public static final long MEMBER_ID = 1L;

    public static Member member() {
        return new Member(MEMBER_ID);
    }
}
