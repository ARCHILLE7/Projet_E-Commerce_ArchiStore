package com.gestion_produit.order_service.service;

import com.gestion_produit.order_service.entity.*;
import com.gestion_produit.order_service.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public Cart addToCart(Long userId, Long productId, Integer quantity) {
        log.info("Adding product {} to cart for user {}", productId, userId);

        // Récupérer ou créer le panier
        Cart cart = cartRepository.findByUserIdAndStatus(userId, "ACTIVE");
        if (cart == null) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            cart = Cart.builder()
                    .user(user)
                    .items(new ArrayList<>())
                    .totalAmount(BigDecimal.ZERO)
                    .build();
            
            cart = cartRepository.save(cart);
            log.info("Created new cart for user {}", userId);
        }

        // Récupérer le produit
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Vérifier si le produit est déjà dans le panier
        CartItem existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            // Mettre à jour la quantité
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            cartItemRepository.save(existingItem);
            log.info("Updated quantity for product {} in cart", productId);
        } else {
            // Ajouter un nouvel item
            CartItem cartItem = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(quantity)
                    .price(product.getPrix())
                    .build();
            
            cartItemRepository.save(cartItem);
            cart.getItems().add(cartItem);
            log.info("Added new item for product {} to cart", productId);
        }

        // Recalculer le total
        cart.calculateTotal();
        cartRepository.save(cart);

        return cart;
    }

    public Cart getCartByUserId(Long userId) {
        Cart cart = cartRepository.findByUserIdAndStatus(userId, "ACTIVE");
        if (cart != null) {
            cart.calculateTotal();
        }
        return cart;
    }

    public void removeFromCart(Long cartItemId) {
        log.info("Removing cart item {}", cartItemId);
        
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));
        
        Cart cart = cartItem.getCart();
        cart.getItems().remove(cartItem);
        
        cartItemRepository.delete(cartItem);
        
        cart.calculateTotal();
        cartRepository.save(cart);
        
        log.info("Removed item {} from cart {}", cartItemId, cart.getId());
    }

    public Cart checkout(Long userId) {
        log.info("Checking out cart for user {}", userId);
        
        Cart cart = cartRepository.findByUserIdAndStatus(userId, "ACTIVE");
        if (cart == null || cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        // Mettre à jour le statut du panier
        cart.setStatus("COMPLETED");
        cartRepository.save(cart);
        
        log.info("Checked out cart {} for user {}", cart.getId(), userId);
        return cart;
    }

    public void clearCart(Long userId) {
        log.info("Clearing cart for user {}", userId);
        
        Cart cart = cartRepository.findByUserIdAndStatus(userId, "ACTIVE");
        if (cart != null) {
            cartItemRepository.deleteByCartId(cart.getId());
            cart.getItems().clear();
            cart.setTotalAmount(BigDecimal.ZERO);
            cartRepository.save(cart);
            
            log.info("Cleared cart {} for user {}", cart.getId(), userId);
        }
    }
}
