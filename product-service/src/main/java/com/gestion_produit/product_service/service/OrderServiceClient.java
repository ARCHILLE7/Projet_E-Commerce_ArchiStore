package com.gestion_produit.product_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceClient {

    private final WebClient.Builder webClientBuilder;

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
}
