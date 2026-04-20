package com.gestion_produit.order_service.config;

import com.gestion_produit.order_service.entity.Order;
import com.gestion_produit.order_service.entity.OrderProduct;
import com.gestion_produit.order_service.repository.OrderRepository;
import com.gestion_produit.order_service.service.ProductServiceClient;
import com.gestion_produit.order_service.service.UserServiceClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final OrderRepository orderRepository;
    private final UserServiceClient userServiceClient;
    private final ProductServiceClient productServiceClient;

    @Override
    public void run(String... args) throws Exception {
        if (orderRepository.count() == 0) {
            log.info("Initializing default orders data...");
            
            // Create sample orders
            List<Order> orders = Arrays.asList(
                createOrder1(),
                createOrder2(),
                createOrder3(),
                createOrder4(),
                createOrder5()
            );

            orderRepository.saveAll(orders);
            log.info("Default orders initialized successfully! {} orders added.", orders.size());
        }
    }

    private Order createOrder1() {
        Order order = Order.builder()
                .utilisateurId(2L) // Jean Dupont
                .orderDate(LocalDateTime.now().minusDays(5))
                .total(new BigDecimal("12999.00"))
                .status(Order.OrderStatus.DELIVERED)
                .shippingAddress("456 Avenue Hassan, 20000 Casablanca")
                .billingAddress("456 Avenue Hassan, 20000 Casablanca")
                .trackingNumber("TRK123456789")
                .estimatedDelivery(LocalDateTime.now().minusDays(2))
                .createdAt(LocalDateTime.now().minusDays(5))
                .build();

        OrderProduct item = OrderProduct.builder()
                .order(order)
                .produitId(1L)
                .quantite(1)
                .unitPrice(new BigDecimal("12999.00"))
                .totalPrice(new BigDecimal("12999.00"))
                .productName("Laptop Dell XPS 15")
                .build();

        order.setOrderProducts(Arrays.asList(item));
        return order;
    }

    private Order createOrder2() {
        Order order = Order.builder()
                .utilisateurId(3L) // Marie Martin
                .orderDate(LocalDateTime.now().minusDays(3))
                .total(new BigDecimal("9498.00"))
                .status(Order.OrderStatus.SHIPPED)
                .shippingAddress("789 Boulevard Mohammed, 30000 Marrakech")
                .billingAddress("789 Boulevard Mohammed, 30000 Marrakech")
                .trackingNumber("TRK987654321")
                .estimatedDelivery(LocalDateTime.now().plusDays(2))
                .createdAt(LocalDateTime.now().minusDays(3))
                .build();

        OrderProduct item1 = OrderProduct.builder()
                .order(order)
                .produitId(2L)
                .quantite(1)
                .unitPrice(new BigDecimal("8999.00"))
                .totalPrice(new BigDecimal("8999.00"))
                .productName("Smartphone Samsung Galaxy S23")
                .build();

        OrderProduct item2 = OrderProduct.builder()
                .order(order)
                .produitId(6L)
                .quantite(1)
                .unitPrice(new BigDecimal("899.00"))
                .totalPrice(new BigDecimal("899.00"))
                .productName("Enceinte Bluetooth JBL Flip 6")
                .build();

        order.setOrderProducts(Arrays.asList(item1, item2));
        return order;
    }

    private Order createOrder3() {
        Order order = Order.builder()
                .utilisateurId(2L) // Jean Dupont
                .orderDate(LocalDateTime.now().minusDays(1))
                .total(new BigDecimal("4599.00"))
                .status(Order.OrderStatus.CONFIRMED)
                .shippingAddress("456 Avenue Hassan, 20000 Casablanca")
                .billingAddress("456 Avenue Hassan, 20000 Casablanca")
                .trackingNumber(null)
                .estimatedDelivery(LocalDateTime.now().plusDays(5))
                .createdAt(LocalDateTime.now().minusDays(1))
                .build();

        OrderProduct item = OrderProduct.builder()
                .order(order)
                .produitId(5L)
                .quantite(1)
                .unitPrice(new BigDecimal("4599.00"))
                .totalPrice(new BigDecimal("4599.00"))
                .productName("Montre Connectée Apple Watch Series 8")
                .build();

        order.setOrderProducts(Arrays.asList(item));
        return order;
    }

    private Order createOrder4() {
        Order order = Order.builder()
                .utilisateurId(4L) // Ahmed Benali
                .orderDate(LocalDateTime.now().minusHours(6))
                .total(new BigDecimal("4398.00"))
                .status(Order.OrderStatus.PENDING)
                .shippingAddress("321 Rue Al Massa, 40000 Fès")
                .billingAddress("321 Rue Al Massa, 40000 Fès")
                .trackingNumber(null)
                .estimatedDelivery(null)
                .createdAt(LocalDateTime.now().minusHours(6))
                .build();

        OrderProduct item1 = OrderProduct.builder()
                .order(order)
                .produitId(3L)
                .quantite(1)
                .unitPrice(new BigDecimal("3499.00"))
                .totalPrice(new BigDecimal("3499.00"))
                .productName("Casque Audio Sony WH-1000XM5")
                .build();

        OrderProduct item2 = OrderProduct.builder()
                .order(order)
                .produitId(7L)
                .quantite(1)
                .unitPrice(new BigDecimal("1299.00"))
                .totalPrice(new BigDecimal("1299.00"))
                .productName("Clavier Mécanique Logitech MX Keys")
                .build();

        order.setOrderProducts(Arrays.asList(item1, item2));
        return order;
    }

    private Order createOrder5() {
        Order order = Order.builder()
                .utilisateurId(5L) // Fatima Zahra
                .orderDate(LocalDateTime.now().minusHours(12))
                .total(new BigDecimal("1798.00"))
                .status(Order.OrderStatus.CANCELLED)
                .shippingAddress("654 Place Yacoub, 50000 Agadir")
                .billingAddress("654 Place Yacoub, 50000 Agadir")
                .trackingNumber(null)
                .estimatedDelivery(null)
                .createdAt(LocalDateTime.now().minusHours(12))
                .build();

        OrderProduct item1 = OrderProduct.builder()
                .order(order)
                .produitId(8L)
                .quantite(1)
                .unitPrice(new BigDecimal("699.00"))
                .totalPrice(new BigDecimal("699.00"))
                .productName("Souris Gaming Logitech G502")
                .build();

        OrderProduct item2 = OrderProduct.builder()
                .order(order)
                .produitId(10L)
                .quantite(1)
                .unitPrice(new BigDecimal("799.00"))
                .totalPrice(new BigDecimal("799.00"))
                .productName("Webcam Logitech StreamCam")
                .build();

        OrderProduct item3 = OrderProduct.builder()
                .order(order)
                .produitId(11L)
                .quantite(1)
                .unitPrice(new BigDecimal("1499.00"))
                .totalPrice(new BigDecimal("1499.00"))
                .productName("Disque SSD externe Samsung T7")
                .build();

        order.setOrderProducts(Arrays.asList(item1, item2, item3));
        return order;
    }
}
