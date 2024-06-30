package com.seong.playground.common;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository {

    Optional<Member> findById(Long memberId);
}
