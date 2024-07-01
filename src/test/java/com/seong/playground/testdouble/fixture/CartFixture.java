package com.seong.playground.testdouble.fixture;

import com.seong.playground.common.Member;
import com.seong.playground.testdouble.domain.Cart;

public class CartFixture {

    public static final long CART_ID = 1L;

    public static Cart cart(Member member) {
        return new Cart(CART_ID, member);
    }
}
