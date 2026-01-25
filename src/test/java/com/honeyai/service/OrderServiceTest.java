package com.honeyai.service;

import com.honeyai.enums.HoneyType;
import com.honeyai.enums.OrderStatus;
import com.honeyai.exception.InvalidStatusTransitionException;
import com.honeyai.model.Client;
import com.honeyai.model.Order;
import com.honeyai.model.OrderLine;
import com.honeyai.model.Product;
import com.honeyai.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductService productService;

    @InjectMocks
    private OrderService orderService;

    private Client client;
    private Product product1;
    private Product product2;
    private Order order;

    @BeforeEach
    void setUp() {
        client = Client.builder()
                .id(1L)
                .name("Test Client")
                .build();

        product1 = Product.builder()
                .id(1L)
                .name("Miel Toutes Fleurs 500g")
                .type(HoneyType.TOUTES_FLEURS)
                .unit("pot 500g")
                .build();

        product2 = Product.builder()
                .id(2L)
                .name("Miel Forêt 1kg")
                .type(HoneyType.FORET)
                .unit("pot 1kg")
                .build();

        order = Order.builder()
                        .id(1L)
                        .client(client)
                        .orderDate(LocalDate.now())
                        .status(OrderStatus.ORDERED)
                        .build();
    }

    @Test
    void findAll_shouldReturnAllOrders() {
        // Given
        when(orderRepository.findAll()).thenReturn(List.of(order));

        // When
        List<Order> result = orderService.findAll();

        // Then
        assertThat(result).hasSize(1);
        verify(orderRepository).findAll();
    }

    @Test
    void findById_shouldReturnOrder_whenExists() {
        // Given
        when(orderRepository.findByIdWithClient(1L)).thenReturn(Optional.of(order));

        // When
        Optional<Order> result = orderService.findById(1L);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1L);
    }

    @Test
    void findByClientId_shouldReturnOrdersSortedByDateDesc() {
        // Given
        Order older = Order.builder()
                .id(2L)
                .client(client)
                .orderDate(LocalDate.of(2024, 1, 1))
                .build();
        when(orderRepository.findByClientIdOrderByOrderDateDesc((1L)))
                .thenReturn(Arrays.asList(order, older));

        // When
        List<Order> result = orderService.findByClientId(1L);

        // Then
        assertThat(result).hasSize(2);
        verify(orderRepository).findByClientIdOrderByOrderDateDesc(1L);
    }

    @Test
    void findByStatus_shouldReturnOrdersWithStatus() {
        // Given
        when(orderRepository.findByStatus(OrderStatus.ORDERED))
                .thenReturn(List.of(order));

        // When
        List<Order> result = orderService.findByStatus(OrderStatus.ORDERED);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getStatus()).isEqualTo(OrderStatus.ORDERED);
    }

    @Test
    void create_shouldAutoPopulatePrices_whenNotProvided() {
        // Given
        OrderLine ligne = OrderLine.builder()
                                    .product(product1)
                                    .quantity(2)
                                    .build();
        order.addLigne(ligne);

        when(productService.getCurrentYearPrice(1L)).thenReturn(new BigDecimal("8.00"));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        // When
        Order result = orderService.create(order);

        // Then
        assertThat(result.getLines().getFirst().getUnitPrice())
                .isEqualByComparingTo(new BigDecimal("8.00"));
        verify(productService).getCurrentYearPrice(1L);
    }

    @Test
    void create_shouldAutoPopulateDateOrder_whenNull() {
        // Given
        OrderLine ligne = OrderLine.builder()
                .product(product1)
                .quantity(2)
                .unitPrice(new BigDecimal("8.00"))
                .build();

        Order newOrder = Order.builder()
                .client(client)
                .build();
        newOrder.addLigne(ligne);

        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArgument(0));

        // When
        Order result = orderService.create(newOrder);

        // Then
        assertThat(result.getOrderDate()).isEqualTo(LocalDate.now());
    }

    @Test
    void create_shouldSetDefaultStatus_whenNull() {
        // Given
        OrderLine ligne = OrderLine.builder()
                .product(product1)
                .quantity(2)
                .unitPrice(new BigDecimal("8.00"))
                .build();

        Order newOrder = Order.builder()
                .client(client)
                .orderDate(LocalDate.now())
                .status(null)
                .build();
        newOrder.addLigne(ligne);

        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArgument(0));

        // When
        Order result = orderService.create(newOrder);

        // Then
        assertThat(result.getStatus()).isEqualTo(OrderStatus.ORDERED);
    }

    @Test
    void create_shouldThrowException_whenNoLignes() {
        // Given
        Order emptyOrder = Order.builder()
                .client(client)
                .orderDate(LocalDate.now())
                .build();

        // When/Then
        assertThatThrownBy(() -> orderService.create(emptyOrder))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("au moins une ligne");
    }

    @Test
    void calculateTotal_shouldSumAllLignes() {
        // Given
        OrderLine ligne1 = OrderLine.builder()
                .product(product1)
                .quantity(2)
                .unitPrice(new BigDecimal("8.00"))
                .build();
        OrderLine ligne2 = OrderLine.builder()
                .product(product2)
                .quantity(1)
                .unitPrice(new BigDecimal("17.00"))
                .build();
        order.addLigne(ligne1);
        order.addLigne(ligne2);

        when(orderRepository.findByIdWithClient(1L)).thenReturn(Optional.of(order));

        // When
        BigDecimal total = orderService.calculateTotal(1L);

        // Then - (2 * 8.00) + (1 * 17.00) = 33.00
        assertThat(total).isEqualByComparingTo(new BigDecimal("33.00"));
    }

    @Test
    void updateStatus_shouldTransition_fromOrderedToRecovered() {
        // Given
        when(orderRepository.findByIdWithClient(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArgument(0));

        // When
        Order result = orderService.updateStatus(1L, OrderStatus.RECOVERED);

        // Then
        assertThat(result.getStatus()).isEqualTo(OrderStatus.RECOVERED);
    }

    @Test
    void updateStatus_shouldTransition_fromRecoveredToPaid() {
        // Given
        order.setStatus(OrderStatus.RECOVERED);
        when(orderRepository.findByIdWithClient(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArgument(0));

        // When
        Order result = orderService.updateStatus(1L, OrderStatus.PAID);

        // Then
        assertThat(result.getStatus()).isEqualTo(OrderStatus.PAID);
    }

    @Test
    void updateStatus_shouldThrowException_whenBackwardTransition() {
        // Given
        order.setStatus(OrderStatus.PAID);
        when(orderRepository.findByIdWithClient(1L)).thenReturn(Optional.of(order));

        // When/Then
        assertThatThrownBy(() -> orderService.updateStatus(1L, OrderStatus.ORDERED))
                .isInstanceOf(InvalidStatusTransitionException.class)
                .hasMessageContaining("PAID")
                .hasMessageContaining("ORDERED");
    }

    @Test
    void updateStatus_shouldThrowException_whenSkippingStatus() {
        // Given - trying to go from COMMANDEE directly to PAYEE
        when(orderRepository.findByIdWithClient(1L)).thenReturn(Optional.of(order));

        // When/Then
        assertThatThrownBy(() -> orderService.updateStatus(1L, OrderStatus.PAID))
                .isInstanceOf(InvalidStatusTransitionException.class)
                .hasMessageContaining("ORDERED")
                .hasMessageContaining("PAID");
    }

    @Test
    void updateStatus_shouldThrowException_whenOrderNotFound() {
        // Given
        when(orderRepository.findByIdWithClient(999L)).thenReturn(Optional.empty());

        // When/Then
        assertThatThrownBy(() -> orderService.updateStatus(999L, OrderStatus.RECOVERED))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("999");
    }
}
