package com.seong.playground.testdouble.domain;

import lombok.Getter;

@Getter
public class CartItem {

    private final Long cartItemId;

    private final Product product;

    private final int amount;

    private final CartItemStatus status = CartItemStatus.CHECK;

    public CartItem(Long cartItemId, Product product, int amount) {
        this.cartItemId = cartItemId;
        this.product = product;
        this.amount = amount;
    }

    public static CartItem create(Long cartItemId, Product product, int amount) {
        return new CartItem(cartItemId, product, amount);
    }

    public boolean isChecked() {
        return status == CartItemStatus.CHECK;
    }
}
