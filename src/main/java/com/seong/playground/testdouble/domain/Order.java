package com.seong.playground.testdouble.domain;

import com.seong.playground.common.Member;
import com.seong.playground.testdouble.service.OrderItemRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class Order {

    private final Long orderId;

    private final Member member;

    private final List<OrderItem> orderItems = new ArrayList<>();

    public Order(Long orderId, Member member) {
        this.orderId = orderId;
        this.member = member;
    }

    public static Order create(Long orderId, Member member, List<OrderItem> orderItems) {
        Order order = new Order(orderId, member);
        order.addAll(orderItems);
        return order;
    }


    public static Order create(long nextId, Cart cart, OrderItemRepository orderItemRepository) {
        List<OrderItem> list = cart.getProductsToBuy().stream()
                .map(cartItem -> OrderItem.create(
                        orderItemRepository.getNextId(),
                        cartItem
                )).toList();
        return create(nextId, cart.getMember(), list);
    }

    private void addAll(List<OrderItem> orderItems) {
        this.orderItems.addAll(orderItems);
    }
}
