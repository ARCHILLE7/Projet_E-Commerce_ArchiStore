package com.gestion_produit.order_service.service;

import com.gestion_produit.order_service.dto.ProductDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceClient {

    private final WebClient.Builder webClientBuilder;

    public ProductDTO getProductById(Long productId) {
        try {
            log.info("Fetching product by id: {}", productId);
            
            return webClientBuilder.build()
                    .get()
                    .uri("http://product-service:8081/api/products/" + productId)
                    .retrieve()
                    .bodyToMono(ProductDTO.class)
                    .block();
                    
        } catch (Exception e) {
            log.error("Error fetching product by id: {}", productId, e);
            return null;
        }
    }

    public List<ProductDTO> getAllProducts() {
        try {
            log.info("Fetching all products");
            
            return webClientBuilder.build()
                    .get()
                    .uri("http://product-service:8081/api/products")
                    .retrieve()
                    .bodyToFlux(ProductDTO.class)
                    .collectList()
                    .block();
                    
        } catch (Exception e) {
            log.error("Error fetching all products", e);
            return List.of();
        }
    }

    public void reduceStock(Long productId, Integer quantity) {
        try {
            log.info("Reducing stock for product {} by {}", productId, quantity);
            
            webClientBuilder.build()
                    .put()
                    .uri("http://product-service:8081/api/products/" + productId + "/reduce-stock?quantity=" + quantity)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();
                    
            log.info("Stock reduced successfully for product {}", productId);
                    
        } catch (Exception e) {
            log.error("Error reducing product stock: {}", productId, e);
            throw new RuntimeException("Failed to reduce product stock", e);
        }
    }

    public boolean isProductUsedInOrders(Long productId) {
        try {
            log.info("Checking if product {} is used in orders", productId);
            
            Boolean isUsed = webClientBuilder.build()
                    .get()
                    .uri("http://order-service:8083/api/orders/product-used/" + productId)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block();
                    
            return isUsed != null && isUsed;
                    
        } catch (Exception e) {
            log.error("Error checking if product is used in orders: {}", productId, e);
            // If we can't check, assume it's used to be safe
            return true;
        }
    }

    public boolean checkProductAvailability(Long productId, Integer quantity) {
        try {
            log.info("Checking availability for product {} quantity {}", productId, quantity);
            
            Boolean isAvailable = webClientBuilder.build()
                    .get()
                    .uri("http://product-service:8081/api/products/" + productId + "/availability?requiredQuantity=" + quantity)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block();
                    
            return isAvailable != null && isAvailable;
                    
        } catch (Exception e) {
            log.error("Error checking product availability: {}", productId, e);
            return false;
        }
    }

    public void reduceProductStock(Long productId, Integer quantity) {
        try {
            log.info("Reducing stock for product {} by {}", productId, quantity);
            
            webClientBuilder.build()
                    .put()
                    .uri("http://product-service:8081/api/products/" + productId + "/reduce-stock?quantity=" + quantity)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();
                    
            log.info("Stock reduced successfully for product {}", productId);
                    
        } catch (Exception e) {
            log.error("Error reducing product stock: {}", productId, e);
            throw new RuntimeException("Failed to reduce product stock", e);
        }
    }
}
