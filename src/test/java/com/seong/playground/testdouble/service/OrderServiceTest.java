package com.seong.playground.testdouble.service;

import static org.junit.jupiter.api.Assertions.*;

import com.seong.playground.testdouble.EventPublisher;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

class OrderServiceTest {

    @Nested
    @DisplayName("목 테스트")
    class UsingMock {

        @InjectMocks
        private OrderService orderService;

        @Mock
        private OrderRepository orderRepository;

        @Mock
        private EventPublisher eventPublisher;


        @Test
        @DisplayName("주문이 생성된다.")
        void createOrder() {
            //given

            //when

            //then
        }

        @Test
        @DisplayName("주문이 생성되면 주문 생성 이벤트가 발행된다.")
        void publishCreateOrderEvent() {
            //given

            //when

            //then
        }

        @Test
        @DisplayName("회원이 존재하지 않으면 예외가 발생한다.")
        void exceptionWhenMemberNotFound() {
            //given

            //when

            //then
        }
    }
}