package com.seong.playground.testdouble.service;

import com.seong.playground.common.Member;
import com.seong.playground.testdouble.domain.Cart;
import java.util.Optional;

public interface CartRepository {

    Optional<Cart> findByMember(Member member);
}
