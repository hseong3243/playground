package com.seong.playground.testdouble.repository;

import com.seong.playground.common.Member;
import com.seong.playground.testdouble.domain.Cart;
import com.seong.playground.testdouble.service.CartRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class CartMemoryRepository implements CartRepository {

    private final Map<Long, Cart> database = new HashMap<>();

    @Override
    public Optional<Cart> findByMember(Member member) {
        return database.values().stream()
            .filter(cart -> cart.getMember().equals(member))
            .findAny();
    }
}
