package com.gestion_produit.product_service;

import com.gestion_produit.product_service.dto.ProductDTO;
import com.gestion_produit.product_service.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Rollback
public class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Test
    public void testCreateAndGetProduct() {
        // Given
        ProductDTO productDTO = new ProductDTO();
        productDTO.setNom("Test Product");
        productDTO.setDescription("Test Description");
        productDTO.setPrix(BigDecimal.valueOf(10.99));
        productDTO.setStock(100);

        // When
        ProductDTO createdProduct = productService.createProduct(productDTO);
        ProductDTO retrievedProduct = productService.getProductById(createdProduct.getId());

        // Then
        assertThat(retrievedProduct).isNotNull();
        assertThat(retrievedProduct.getNom()).isEqualTo("Test Product");
        assertThat(retrievedProduct.getPrix()).isEqualByComparingTo(BigDecimal.valueOf(10.99));
    }

}