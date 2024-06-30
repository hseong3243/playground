package com.seong.playground.common;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberDataJpaRepository extends JpaRepository<Member, Long> {

}
