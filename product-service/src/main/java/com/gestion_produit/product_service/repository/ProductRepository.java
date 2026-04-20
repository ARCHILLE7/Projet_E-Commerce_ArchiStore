package com.gestion_produit.product_service.repository;

import com.gestion_produit.product_service.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByIsActive(Boolean isActive);
    
    List<Product> findByStockLessThan(Integer threshold);
    
    List<Product> findByNomContainingIgnoreCase(String keyword);
    
    List<Product> findByPrixBetween(BigDecimal minPrice, BigDecimal maxPrice);
    
    @Query("SELECT p FROM Product p WHERE p.stock < :threshold AND p.isActive = true")
    List<Product> findActiveProductsWithLowStock(Integer threshold);
    
    @Query("SELECT COUNT(p) FROM Product p WHERE p.isActive = true")
    long countActiveProducts();
    
    boolean existsByNom(String nom);
}
