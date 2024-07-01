package com.seong.playground.testdouble.stub;

import com.seong.playground.testdouble.domain.Order;
import com.seong.playground.testdouble.service.OrderRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class StubOrderRepository implements OrderRepository {

    private final Map<Long, Order> database = new HashMap<>();

    public void stub(Order order) {
        database.put(getNextId(), order);
    }

    @Override
    public long save(Order order) {
        stub(order);
        return database.size();
    }

    @Override
    public long getNextId() {
        return database.size() + 1;
    }

    @Override
    public Optional<Order> findById(Long orderId) {
        return Optional.ofNullable(database.get(orderId));
    }
}
