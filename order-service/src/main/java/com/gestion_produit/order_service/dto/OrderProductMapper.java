package com.gestion_produit.order_service.dto;

import com.gestion_produit.order_service.entity.Order;
import com.gestion_produit.order_service.entity.OrderProduct;
import org.springframework.stereotype.Component;

@Component
public class OrderProductMapper {

    public OrderProductDTO toDTO(OrderProduct orderProduct) {
        if (orderProduct == null) {
            return null;
        }
        OrderProductDTO dto = new OrderProductDTO();
        dto.setId(orderProduct.getId());
        dto.setOrderId(orderProduct.getOrder().getId());
        dto.setProduitId(orderProduct.getProduitId());
        dto.setQuantite(orderProduct.getQuantite());
        return dto;
    }

    public OrderProduct toEntity(OrderProductDTO dto, Order order) {
        if (dto == null) {
            return null;
        }
        OrderProduct orderProduct = new OrderProduct();
        orderProduct.setId(dto.getId());
        orderProduct.setOrder(order);
        orderProduct.setProduitId(dto.getProduitId());
        orderProduct.setQuantite(dto.getQuantite());
        return orderProduct;
    }

}