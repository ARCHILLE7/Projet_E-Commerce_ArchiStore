package com.gestion_produit.security_service.controller;

import com.gestion_produit.security_service.dto.AuthRequest;
import com.gestion_produit.security_service.dto.AuthResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/simple-auth")
@Slf4j
@CrossOrigin(origins = "*")
public class SimpleAuthController {

    // In-memory user store for testing
    private static final Map<String, String> userStore = new HashMap<>();
    private static final Map<String, AuthResponse> sessionStore = new HashMap<>();
    
    static {
        // Store users with plain text passwords for testing
        userStore.put("admin@example.com", "Admin123!");
        userStore.put("user@example.com", "User123!");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        log.info("Simple login attempt for user: {}", request.getEmail());
        
        try {
            // Verify credentials
            String storedPassword = userStore.get(request.getEmail());
            if (storedPassword == null || !storedPassword.equals(request.getPassword())) {
                log.error("Invalid credentials for user: {}", request.getEmail());
                return ResponseEntity.status(403).build();
            }
            
            // Create simple session token
            String sessionToken = UUID.randomUUID().toString();
            String role = request.getEmail().equals("admin@example.com") ? "ADMIN" : "USER";
            
            AuthResponse response = AuthResponse.builder()
                .token(sessionToken)
                .type("Simple")
                .userId(request.getEmail().equals("admin@example.com") ? 1L : 2L)
                .email(request.getEmail())
                .role(role)
                .build();
            
            // Store session
            sessionStore.put(sessionToken, response);
            
            log.info("Simple login successful for user: {}", request.getEmail());
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Simple login failed for user: {}", request.getEmail(), e);
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/validate")
    public ResponseEntity<Boolean> validateToken(@RequestParam String token) {
        try {
            boolean isValid = sessionStore.containsKey(token);
            log.info("Token validation result: {}", isValid);
            return ResponseEntity.ok(isValid);
        } catch (Exception e) {
            log.error("Token validation failed", e);
            return ResponseEntity.ok(false);
        }
    }

    @GetMapping("/user")
    public ResponseEntity<AuthResponse> getUser(@RequestParam String token) {
        try {
            AuthResponse user = sessionStore.get(token);
            if (user != null) {
                return ResponseEntity.ok(user);
            } else {
                return ResponseEntity.status(401).build();
            }
        } catch (Exception e) {
            log.error("Get user failed", e);
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestParam String token) {
        try {
            sessionStore.remove(token);
            log.info("User logged out successfully");
            return ResponseEntity.ok("Logged out successfully");
        } catch (Exception e) {
            log.error("Logout failed", e);
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Simple auth controller is working!");
    }
}
