package com.seong.playground.testdouble.stub;

import com.seong.playground.testdouble.domain.Order;
import com.seong.playground.testdouble.domain.OrderItem;
import com.seong.playground.testdouble.service.OrderItemRepository;
import java.util.HashMap;
import java.util.Map;

public class StubOrderItemRepository implements OrderItemRepository {

    private final Map<Long, OrderItem> database = new HashMap<>();

    public void stub(OrderItem orderItem) {
        database.put(getNextId(), orderItem);
    }

    @Override
    public long getNextId() {
        return database.size() + 1;
    }
}
