package com.gestion_produit.user_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/simple-cart")
@CrossOrigin(origins = "*")
public class SimpleCartController {

    // Stockage simple en mémoire pour le panier
    private static Map<Long, Map<String, Object>> userCarts = new HashMap<>();
    private static Map<Long, List<Map<String, Object>>> cartItems = new HashMap<>();

    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addToCart(@RequestBody Map<String, Object> request) {
        try {
            Long userId = Long.valueOf(request.get("userId").toString());
            Long productId = Long.valueOf(request.get("productId").toString());
            Integer quantity = Integer.valueOf(request.get("quantity").toString());
            String productName = request.get("productName").toString();
            Double price = Double.valueOf(request.get("price").toString());

            // Initialiser le panier si nécessaire
            userCarts.putIfAbsent(userId, new HashMap<>());
            cartItems.putIfAbsent(userId, new ArrayList<>());

            Map<String, Object> cartItem = new HashMap<>();
            cartItem.put("id", productId);
            cartItem.put("name", productName);
            cartItem.put("price", price);
            cartItem.put("quantity", quantity);
            cartItem.put("subtotal", price * quantity);

            // Ajouter l'item au panier
            cartItems.get(userId).add(cartItem);

            // Calculer le total
            double total = cartItems.get(userId).stream()
                    .mapToDouble(item -> (Double) item.get("subtotal"))
                    .sum();

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Produit ajouté au panier");
            response.put("totalItems", cartItems.get(userId).size());
            response.put("totalAmount", total);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Erreur: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Map<String, Object>> getCart(@PathVariable Long userId) {
        try {
            List<Map<String, Object>> items = cartItems.getOrDefault(userId, new ArrayList<>());
            
            double total = items.stream()
                    .mapToDouble(item -> (Double) item.get("subtotal"))
                    .sum();

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("items", items);
            response.put("totalItems", items.size());
            response.put("totalAmount", total);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Erreur: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/remove/{userId}/{productId}")
    public ResponseEntity<Map<String, Object>> removeFromCart(@PathVariable Long userId, @PathVariable Long productId) {
        try {
            List<Map<String, Object>> items = cartItems.get(userId);
            if (items != null) {
                items.removeIf(item -> item.get("id").equals(productId));
            }

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Produit supprimé du panier");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Erreur: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/checkout/{userId}")
    public ResponseEntity<Map<String, Object>> checkout(@PathVariable Long userId) {
        try {
            List<Map<String, Object>> items = cartItems.get(userId);
            if (items == null || items.isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Panier vide");
                return ResponseEntity.badRequest().body(response);
            }

            // Simuler la commande
            double total = items.stream()
                    .mapToDouble(item -> (Double) item.get("subtotal"))
                    .sum();

            // Vider le panier
            cartItems.put(userId, new ArrayList<>());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Commande passée avec succès");
            response.put("orderId", "ORDER-" + System.currentTimeMillis());
            response.put("totalAmount", total);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Erreur: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/clear/{userId}")
    public ResponseEntity<Map<String, Object>> clearCart(@PathVariable Long userId) {
        try {
            cartItems.put(userId, new ArrayList<>());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Panier vidé");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Erreur: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
