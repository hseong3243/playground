package com.seong.playground.testdouble.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;

@Getter
public class CartItem {

    private final Long cartItemId;

    private final Item item;

    private final Integer amount;

    private final CartItemStatus status;

    private final Cart cart;

    public CartItem(Long cartItemId, Item item, Integer amount, CartItemStatus status, Cart cart) {
        this.cartItemId = cartItemId;
        this.item = item;
        this.amount = amount;
        this.status = status;
        this.cart = cart;
    }

    public boolean isChecked() {
        return status == CartItemStatus.CHECK;
    }
}
