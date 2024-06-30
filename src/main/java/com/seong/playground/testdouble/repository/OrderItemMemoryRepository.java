package com.seong.playground.testdouble.repository;

import com.seong.playground.testdouble.domain.OrderItem;
import com.seong.playground.testdouble.service.OrderItemRepository;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderItemMemoryRepository implements OrderItemRepository {

    private final Map<Long, OrderItem> database = new HashMap<>();

    @Override
    public long getNextId() {
        return (long) (database.size() + 1);
    }
}
