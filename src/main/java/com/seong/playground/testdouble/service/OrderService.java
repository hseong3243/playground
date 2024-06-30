package com.seong.playground.testdouble.service;

import com.seong.playground.common.Member;
import com.seong.playground.common.MemberRepository;
import com.seong.playground.testdouble.EventPublisher;
import com.seong.playground.testdouble.domain.Cart;
import com.seong.playground.testdouble.domain.Order;
import com.seong.playground.testdouble.domain.OrderItem;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final EventPublisher eventPublisher;

    public Long createOrder(Long memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(NoSuchElementException::new);
        Cart cart = cartRepository.findByMember(member)
            .orElseThrow(NoSuchElementException::new);

        List<OrderItem> orderItems = cart.getProductsToBuy().stream()
            .map(cartItem -> OrderItem.create(
                orderItemRepository.getNextId(),
                cartItem
            )).toList();
        Order order = Order.create(orderRepository.getNextId(), member, orderItems);
        eventPublisher.publish(CreateOrderEvent.create(order));
        return orderRepository.save(order);
    }
}
