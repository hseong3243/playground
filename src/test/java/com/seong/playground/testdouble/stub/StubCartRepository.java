package com.seong.playground.testdouble.stub;

import com.seong.playground.common.Member;
import com.seong.playground.testdouble.domain.Cart;
import com.seong.playground.testdouble.fixture.CartFixture;
import com.seong.playground.testdouble.service.CartRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class StubCartRepository implements CartRepository {

    private final Map<Long, Cart> database = new HashMap<>();

    public void stub(Cart cart) {
        database.put(getNextId(), cart);
    }

    private Long getNextId() {
        return (long) (database.size() + 1);
    }

    @Override
    public Optional<Cart> findByMember(Member member) {
        if(database.isEmpty()) {
            return Optional.of(CartFixture.cart(member));
        }
        return database.values().stream().findFirst();
    }

    @Override
    public Long save(Cart cart) {
        stub(cart);
        return (long) database.size();
    }
}
