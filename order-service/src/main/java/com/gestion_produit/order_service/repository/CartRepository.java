package com.gestion_produit.order_service.repository;

import com.gestion_produit.order_service.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    
    Optional<Cart> findByUserIdAndStatus(Long userId, String status);
    
    Cart findByUserIdAndStatus(Long userId, String status);
}
