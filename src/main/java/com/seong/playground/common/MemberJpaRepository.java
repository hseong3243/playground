package com.seong.playground.common;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberJpaRepository implements MemberRepository{

    private final MemberDataJpaRepository memberDataJpaRepository;


    @Override
    public Optional<Member> findById(Long memberId) {
        return memberDataJpaRepository.findById(memberId);
    }
}
