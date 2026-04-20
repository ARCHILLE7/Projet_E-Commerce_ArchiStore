package com.gestion_produit.order_service.controller;

import com.gestion_produit.order_service.entity.Cart;
import com.gestion_produit.order_service.entity.CartItem;
import com.gestion_produit.order_service.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class CartController {

    private final CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addToCart(@RequestBody Map<String, Object> request) {
        try {
            Long userId = Long.valueOf(request.get("userId").toString());
            Long productId = Long.valueOf(request.get("productId").toString());
            Integer quantity = Integer.valueOf(request.get("quantity").toString());

            Cart cart = cartService.addToCart(userId, productId, quantity);
            
            Map<String, Object> response = Map.of(
                "success", true,
                "message", "Product added to cart",
                "cartId", cart.getId(),
                "totalItems", cart.getItems().size()
            );
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error adding to cart", e);
            Map<String, Object> response = Map.of(
                "success", false,
                "message", "Error: " + e.getMessage()
            );
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Map<String, Object>> getCart(@PathVariable Long userId) {
        try {
            Cart cart = cartService.getCartByUserId(userId);
            
            Map<String, Object> response = Map.of(
                "success", true,
                "cart", cart,
                "totalItems", cart.getItems().size(),
                "totalAmount", cart.getTotalAmount()
            );
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error getting cart", e);
            Map<String, Object> response = Map.of(
                "success", false,
                "message", "Error: " + e.getMessage()
            );
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/remove/{cartItemId}")
    public ResponseEntity<Map<String, Object>> removeFromCart(@PathVariable Long cartItemId) {
        try {
            cartService.removeFromCart(cartItemId);
            
            Map<String, Object> response = Map.of(
                "success", true,
                "message", "Product removed from cart"
            );
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error removing from cart", e);
            Map<String, Object> response = Map.of(
                "success", false,
                "message", "Error: " + e.getMessage()
            );
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/checkout")
    public ResponseEntity<Map<String, Object>> checkout(@RequestBody Map<String, Object> request) {
        try {
            Long userId = Long.valueOf(request.get("userId").toString());
            Cart cart = cartService.checkout(userId);
            
            Map<String, Object> response = Map.of(
                "success", true,
                "message", "Order placed successfully",
                "orderId", cart.getId()
            );
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error during checkout", e);
            Map<String, Object> response = Map.of(
                "success", false,
                "message", "Error: " + e.getMessage()
            );
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/clear/{userId}")
    public ResponseEntity<Map<String, Object>> clearCart(@PathVariable Long userId) {
        try {
            cartService.clearCart(userId);
            
            Map<String, Object> response = Map.of(
                "success", true,
                "message", "Cart cleared"
            );
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error clearing cart", e);
            Map<String, Object> response = Map.of(
                "success", false,
                "message", "Error: " + e.getMessage()
            );
            return ResponseEntity.badRequest().body(response);
        }
    }
}
