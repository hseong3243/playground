package com.seong.playground.testdouble.repository;

import com.seong.playground.testdouble.domain.Order;
import com.seong.playground.testdouble.service.OrderRepository;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderMemoryRepository implements OrderRepository {

    private final Map<Long, Order> database = new HashMap<>();

    @Override
    public long save(Order order) {
        long nextId = getNextId();
        database.put(nextId, order);
        return nextId;
    }

    @Override
    public long getNextId() {
        return database.size() + 1;
    }
}
