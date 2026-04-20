package com.gestion_produit.order_service.service;

import com.gestion_produit.order_service.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceClient {

    private final WebClient.Builder webClientBuilder;

    public UserDTO getUserById(Long userId) {
        try {
            log.info("Fetching user by id: {}", userId);
            
            return webClientBuilder.build()
                    .get()
                    .uri("http://user-service:8082/api/users/" + userId)
                    .retrieve()
                    .bodyToMono(UserDTO.class)
                    .block();
                    
        } catch (Exception e) {
            log.error("Error fetching user by id: {}", userId, e);
            return null;
        }
    }

    public boolean userExists(Long userId) {
        try {
            log.info("Checking if user {} exists", userId);
            
            Boolean exists = webClientBuilder.build()
                    .get()
                    .uri("http://user-service:8082/api/users/" + userId + "/active-status")
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block();
                    
            return exists != null && exists;
                    
        } catch (Exception e) {
            log.error("Error checking if user exists: {}", userId, e);
            return false;
        }
    }

    public boolean isUserActive(Long userId) {
        try {
            log.info("Checking if user {} is active", userId);
            
            Boolean isActive = webClientBuilder.build()
                    .get()
                    .uri("http://user-service:8082/api/users/" + userId + "/active-status")
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block();
                    
            return isActive != null && isActive;
                    
        } catch (Exception e) {
            log.error("Error checking if user is active: {}", userId, e);
            return false;
        }
    }
}
