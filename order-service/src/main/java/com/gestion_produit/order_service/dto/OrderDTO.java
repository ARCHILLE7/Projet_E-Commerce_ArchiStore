package com.gestion_produit.order_service.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {

    private Long id;

    @NotNull(message = "L'utilisateur est obligatoire")
    private Long utilisateurId;

    private LocalDateTime orderDate;

    private BigDecimal total;

    private List<OrderProductDTO> orderProducts;

}