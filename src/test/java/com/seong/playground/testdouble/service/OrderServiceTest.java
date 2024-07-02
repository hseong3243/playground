package com.seong.playground.testdouble.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import com.seong.playground.common.Member;
import com.seong.playground.common.MemberRepository;
import com.seong.playground.testdouble.EventPublisher;
import com.seong.playground.testdouble.domain.Cart;
import com.seong.playground.testdouble.domain.CartItem;
import com.seong.playground.testdouble.domain.Order;
import com.seong.playground.testdouble.domain.Product;
import com.seong.playground.testdouble.fixture.CartFixture;
import com.seong.playground.testdouble.fixture.MemberFixture;
import com.seong.playground.testdouble.repository.CartMemoryRepository;
import com.seong.playground.testdouble.repository.MemberMemoryRepository;
import com.seong.playground.testdouble.repository.OrderItemMemoryRepository;
import com.seong.playground.testdouble.repository.OrderMemoryRepository;
import com.seong.playground.testdouble.stub.SpyEventPublisher;
import com.seong.playground.testdouble.stub.StubCartRepository;
import com.seong.playground.testdouble.stub.StubMemberRepository;
import com.seong.playground.testdouble.stub.StubOrderItemRepository;
import com.seong.playground.testdouble.stub.StubOrderRepository;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Nested
    @DisplayName("더미 테스트")
    class UsingDummy {
        @InjectMocks
        private OrderService orderService;

        @Mock
        private MemberRepository memberRepository;

        @Mock
        private CartRepository cartRepository;

        @Mock
        private OrderItemRepository orderItemRepository;

        @Mock
        private OrderRepository orderRepository;

        @Mock
        private EventPublisher eventPublisher;

        @Test
        @DisplayName("[Mockito] 주문을 조회한다.")
        void findOrder() {
            //given
            Order order = new Order(1L, MemberFixture.member());
            given(orderRepository.findById(any())).willReturn(Optional.of(order));

            //when
            Order findOrder = orderService.findOrder(1L);

            //then
            assertThat(findOrder).isEqualTo(order);
        }
    }

    @Nested
    @DisplayName("목 테스트")
    class UsingMock {

        @InjectMocks
        private OrderService orderService;

        @Mock
        private MemberRepository memberRepository;

        @Mock
        private CartRepository cartRepository;

        @Mock
        private OrderItemRepository orderItemRepository;

        @Mock
        private OrderRepository orderRepository;

        @Mock
        private EventPublisher eventPublisher;

        @Test
        @DisplayName("[Mockito] 주문이 생성된다.")
        void createOrder() {
            //given
            Member member = MemberFixture.member();
            Cart cart = CartFixture.cart(member);
            Product product = Product.create(1L, "상품1", 10);
            cart.addCartItem(CartItem.create(1L, product, 2));

            given(memberRepository.findById(any()))
                    .willReturn(Optional.of(member));
            given(cartRepository.findByMember(any()))
                    .willReturn(Optional.of(cart));
            given(orderItemRepository.getNextId())
                    .willReturn(1L, 2L, 3L);

            //when
            orderService.createOrder(member.getMemberId());

            //then
            then(orderRepository).should().save(any());
        }
    }

    @Nested
    @DisplayName("Spy 테스트")
    class UsingSpy {

        @Nested
        @DisplayName("[Mockito]")
        class WithMockito {

            @InjectMocks
            private OrderService orderService;

            @Mock
            private MemberRepository memberRepository;

            @Mock
            private CartRepository cartRepository;

            @Mock
            private OrderItemRepository orderItemRepository;

            @Mock
            private OrderRepository orderRepository;

            @Mock
            private EventPublisher eventPublisher;

            @Test
            @DisplayName("주문 생성 이벤트가 발행된다.")
            void publishEventWithMockito() {
                //given
                Member member = MemberFixture.member();
                Cart cart = CartFixture.cart(member);
                Product product = Product.create(1L, "상품", 10);
                cart.addCartItem(CartItem.create(1L, product, 2));

                given(memberRepository.findById(any())).willReturn(Optional.of(member));
                given(cartRepository.findByMember(any())).willReturn(Optional.of(cart));
                given(orderItemRepository.getNextId()).willReturn(1L, 2L);

                //when
                orderService.createOrder(member.getMemberId());

                //then
                then(eventPublisher).should(times(1)).publish(any());
            }
        }

        @Nested
        @DisplayName("[Implementation]")
        class WithImplementation {

            private OrderService orderService;
            private StubMemberRepository memberRepository;
            private StubCartRepository cartRepository;
            private StubOrderItemRepository orderItemRepository;
            private StubOrderRepository orderRepository;
            private SpyEventPublisher eventPublisher;

            @BeforeEach
            void setUp() {
                memberRepository = new StubMemberRepository();
                cartRepository = new StubCartRepository();
                orderItemRepository = new StubOrderItemRepository();
                orderRepository = new StubOrderRepository();
                eventPublisher = new SpyEventPublisher();
                orderService = new OrderService(memberRepository, cartRepository, orderItemRepository,
                        orderRepository, eventPublisher);
            }

            @Test
            @DisplayName("주문 생성 이벤트가 발행된다.")
            void publishEventWithMockito() {
                //given
                Member member = MemberFixture.member();
                Cart cart = CartFixture.cart(member);
                Product product = Product.create(1L, "상품", 10);
                cart.addCartItem(CartItem.create(1L, product, 2));

                memberRepository.stub(member);
                cartRepository.stub(cart);

                //when
                orderService.createOrder(member.getMemberId());

                //then
                assertThat(eventPublisher.count).isEqualTo(1);
            }
        }
    }

    @Nested
    @DisplayName("Stub 테스트")
    class StubTest {

        private OrderService orderService;
        private StubMemberRepository memberRepository;
        private StubCartRepository cartRepository;
        private StubOrderItemRepository orderItemRepository;
        private StubOrderRepository orderRepository;
        private SpyEventPublisher eventPublisher;

        @BeforeEach
        void setUp() {
            memberRepository = new StubMemberRepository();
            cartRepository = new StubCartRepository();
            orderItemRepository = new StubOrderItemRepository();
            orderRepository = new StubOrderRepository();
            eventPublisher = new SpyEventPublisher();
            orderService = new OrderService(memberRepository, cartRepository, orderItemRepository,
                orderRepository, eventPublisher);
        }

        @Test
        @DisplayName("[Implementation] 주문이 생성된다.")
        void createOrder() {
            //given
            Member member = MemberFixture.member();
            Cart cart = CartFixture.cart(member);
            Product product = Product.create(1L, "상품1", 10);
            cart.addCartItem(CartItem.create(1L, product, 2));

            memberRepository.stub(member);
            cartRepository.stub(cart);

            //when
            Long orderId = orderService.createOrder(member.getMemberId());

            //then
            Optional<Order> optionalOrder = orderRepository.findById(orderId);
            assertThat(optionalOrder).isPresent().get()
                .satisfies(order -> {
                    assertThat(order.getMember()).isEqualTo(member);
                    assertThat(order.getOrderItems()).hasSize(1)
                        .first()
                        .satisfies(orderItem -> {
                            assertThat(orderItem.getAmount()).isEqualTo(2);
                            assertThat(orderItem.getProduct()).isEqualTo(product);
                        });
                });
        }
    }

    @Nested
    @DisplayName("Fake 테스트")
    class UsingFake {

        private OrderService orderService;
        private MemberMemoryRepository memberRepository;
        private CartMemoryRepository cartRepository;
        private OrderItemMemoryRepository orderItemRepository;
        private OrderMemoryRepository orderRepository;
        private SpyEventPublisher eventPublisher;

        @BeforeEach
        void setUp() {
            memberRepository = new MemberMemoryRepository();
            cartRepository = new CartMemoryRepository();
            orderItemRepository = new OrderItemMemoryRepository();
            orderRepository = new OrderMemoryRepository();
            eventPublisher = new SpyEventPublisher();
            orderService = new OrderService(memberRepository, cartRepository, orderItemRepository,
                orderRepository, eventPublisher);
        }

        @Test
        @DisplayName("[Implementation] 주문이 생성된다.")
        void createOrder() {
            //given
            Member member = MemberFixture.member();
            Cart cart = CartFixture.cart(member);
            Product product = Product.create(1L, "상품1", 10);
            cart.addCartItem(CartItem.create(1L, product, 2));

            memberRepository.save(member);
            cartRepository.save(cart);

            //when
            Long orderId = orderService.createOrder(member.getMemberId());

            //then
            Optional<Order> optionalOrder = orderRepository.findById(orderId);
            assertThat(optionalOrder).isPresent().get()
                .satisfies(order -> {
                    assertThat(order.getMember()).isEqualTo(member);
                    assertThat(order.getOrderItems()).hasSize(1)
                        .first()
                        .satisfies(orderItem -> {
                            assertThat(orderItem.getAmount()).isEqualTo(2);
                            assertThat(orderItem.getProduct()).isEqualTo(product);
                        });
                });
        }
    }
}