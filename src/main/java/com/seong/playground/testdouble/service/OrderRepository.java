package com.seong.playground.testdouble.service;

import com.seong.playground.testdouble.domain.Order;
import java.util.Optional;

public interface OrderRepository {

    long save(Order order);

    long getNextId();
}
