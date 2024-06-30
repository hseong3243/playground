package com.seong.playground.testdouble.domain;

public class OrderItem {

    private final Long orderItemId;

    private final Item item;

    private final Integer amount;

    public OrderItem(Long orderItemId, Item item, Integer amount) {
        this.orderItemId = orderItemId;
        this.item = item;
        this.amount = amount;
    }

    public static OrderItem create(Long orderItemId, CartItem cartItem) {
        return new OrderItem(orderItemId, cartItem.getItem(), cartItem.getAmount());
    }
}
