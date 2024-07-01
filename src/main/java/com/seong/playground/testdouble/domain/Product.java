package com.seong.playground.testdouble.domain;

public class Product {

    private final Long productId;

    private final String name;

    private int amount;

    public Product(Long productId, String name, int amount) {
        this.productId = productId;
        this.name = name;
        this.amount = amount;
    }

    public static Product create(Long productId, String name, int amount) {
        return new Product(productId, name, amount);
    }
}
