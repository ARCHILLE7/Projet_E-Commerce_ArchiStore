package com.gestion_produit.product_service.dto;

import com.gestion_produit.product_service.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductDTO toDTO(Product product) {
        if (product == null) {
            return null;
        }
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setNom(product.getNom());
        dto.setDescription(product.getDescription());
        dto.setPrix(product.getPrix());
        dto.setStock(product.getStock());
        dto.setIsActive(product.getIsActive());
        dto.setImageUrl(product.getImageUrl());
        dto.setCreatedAt(product.getCreatedAt());
        dto.setUpdatedAt(product.getUpdatedAt());
        return dto;
    }

    public Product toEntity(ProductDTO dto) {
        if (dto == null) {
            return null;
        }
        Product product = new Product();
        product.setId(dto.getId());
        product.setNom(dto.getNom());
        product.setDescription(dto.getDescription());
        product.setPrix(dto.getPrix());
        product.setStock(dto.getStock());
        product.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : true);
        product.setImageUrl(dto.getImageUrl());
        return product;
    }
}
