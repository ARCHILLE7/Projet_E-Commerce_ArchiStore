package com.gestion_produit.order_service.dto;

import com.gestion_produit.order_service.entity.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    public OrderDTO toDTO(Order order) {
        if (order == null) {
            return null;
        }
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setUtilisateurId(order.getUtilisateurId());
        dto.setOrderDate(order.getOrderDate());
        dto.setTotal(order.getTotal());
        return dto;
    }

    public Order toEntity(OrderDTO dto) {
        if (dto == null) {
            return null;
        }
        Order order = new Order();
        order.setId(dto.getId());
        order.setUtilisateurId(dto.getUtilisateurId());
        order.setOrderDate(dto.getOrderDate());
        order.setTotal(dto.getTotal());
        return order;
    }

}