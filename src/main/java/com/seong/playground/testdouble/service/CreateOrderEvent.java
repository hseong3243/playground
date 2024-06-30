package com.seong.playground.testdouble.service;

import com.seong.playground.testdouble.Event;
import com.seong.playground.testdouble.domain.Order;

public class CreateOrderEvent extends Event {

    private final Long orderId;

    private CreateOrderEvent(Long orderId) {
        this.orderId = orderId;
    }

    public static CreateOrderEvent create(Order order) {
        return new CreateOrderEvent(order.getOrderId());
    }
}
