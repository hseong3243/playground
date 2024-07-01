package com.seong.playground.testdouble.domain;

import com.seong.playground.common.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class Cart {

    private final Long cartId;

    private final List<CartItem> items = new ArrayList<>();

    private final Member member;

    public Cart(Long cartId, Member member) {
        this.cartId = cartId;
        this.member = member;
    }

    public List<CartItem> getProductsToBuy() {
        return items.stream()
            .filter(CartItem::isChecked)
            .toList();
    }

    public void addCartItem(CartItem cartItem) {
        items.add(cartItem);
    }
}
