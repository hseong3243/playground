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

    @Override
    public Long save(Cart cart) {
        database.put(getNextId(), cart);
        return (long) database.size();
    }

    @Override
    public Optional<Cart> findByMemberId(Long memberId) {
        return database.values().stream().filter(cart -> cart.getMember().getMemberId().equals(memberId))
                .findAny();
    }

    private Long getNextId() {
        return (long) (database.size() + 1);
    }
}
