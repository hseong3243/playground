package com.seong.playground.testdouble.domain;

import lombok.Getter;

@Getter
public class OrderItem {

    private final Long orderItemId;

    private final Product product;

    private final Integer amount;

    public OrderItem(Long orderItemId, Product product, Integer amount) {
        this.orderItemId = orderItemId;
        this.product = product;
        this.amount = amount;
    }

    public static OrderItem create(Long orderItemId, CartItem cartItem) {
        return new OrderItem(orderItemId, cartItem.getProduct(), cartItem.getAmount());
    }
}
