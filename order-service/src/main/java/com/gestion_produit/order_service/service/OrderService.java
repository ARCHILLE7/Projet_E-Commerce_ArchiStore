package com.gestion_produit.order_service.service;

import com.gestion_produit.order_service.dto.*;
import com.gestion_produit.order_service.entity.Order;
import com.gestion_produit.order_service.entity.OrderProduct;
import com.gestion_produit.order_service.exception.ResourceNotFoundException;
import com.gestion_produit.order_service.repository.OrderProductRepository;
import com.gestion_produit.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
    private final OrderMapper orderMapper;
    private final OrderProductMapper orderProductMapper;
    private final ProductServiceClient productServiceClient;
    private final UserServiceClient userServiceClient;

    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(order -> {
                    OrderDTO dto = orderMapper.toDTO(order);
                    dto.setOrderProducts(getOrderProductsByOrderId(order.getId()));
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public OrderDTO getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Commande non trouvée avec l'id: " + id));
        OrderDTO dto = orderMapper.toDTO(order);
        dto.setOrderProducts(getOrderProductsByOrderId(id));
        return dto;
    }

    public OrderDTO createOrder(OrderDTO orderDTO) {
        // Validate user exists
        UserDTO user = userServiceClient.getUserById(orderDTO.getUtilisateurId());
        if (user == null) {
            throw new ResourceNotFoundException("Utilisateur non trouvé");
        }

        // Calculate total and validate products
        BigDecimal total = BigDecimal.ZERO;
        for (OrderProductDTO opDTO : orderDTO.getOrderProducts()) {
            ProductDTO product = productServiceClient.getProductById(opDTO.getProduitId());
            if (product == null) {
                throw new ResourceNotFoundException("Produit non trouvé: " + opDTO.getProduitId());
            }
            if (product.getStock() < opDTO.getQuantite()) {
                throw new IllegalArgumentException("Stock insuffisant pour le produit: " + product.getNom());
            }
            total = total.add(product.getPrix().multiply(BigDecimal.valueOf(opDTO.getQuantite())));
        }

        // Create order
        Order order = new Order();
        order.setUtilisateurId(orderDTO.getUtilisateurId());
        order.setOrderDate(LocalDateTime.now());
        order.setTotal(total);
        Order savedOrder = orderRepository.save(order);

        // Create order products and reduce stock
        for (OrderProductDTO opDTO : orderDTO.getOrderProducts()) {
            OrderProduct orderProduct = new OrderProduct();
            orderProduct.setOrder(savedOrder);
            orderProduct.setProduitId(opDTO.getProduitId());
            orderProduct.setQuantite(opDTO.getQuantite());
            orderProductRepository.save(orderProduct);

            // Reduce stock
            productServiceClient.reduceStock(opDTO.getProduitId(), opDTO.getQuantite());
        }

        OrderDTO result = orderMapper.toDTO(savedOrder);
        result.setOrderProducts(orderDTO.getOrderProducts());
        return result;
    }

    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new ResourceNotFoundException("Commande non trouvée avec l'id: " + id);
        }
        orderProductRepository.deleteByOrderId(id);
        orderRepository.deleteById(id);
    }

    private List<OrderProductDTO> getOrderProductsByOrderId(Long orderId) {
        return orderProductRepository.findByOrderId(orderId).stream()
                .map(orderProductMapper::toDTO)
                .collect(Collectors.toList());
    }

}