package com.gestion_produit.simpleauth.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@Slf4j
@CrossOrigin(origins = "*")
public class AuthController {

    // In-memory user store for testing
    private static final Map<String, String> userStore = new HashMap<>();
    private static final Map<String, Map<String, Object>> sessionStore = new HashMap<>();
    
    static {
        // Store users with plain text passwords for testing
        userStore.put("admin@example.com", "Admin123!");
        userStore.put("user@example.com", "User123!");
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> request) {
        log.info("Simple login attempt for user: {}", request.get("email"));
        
        try {
            String email = request.get("email");
            String password = request.get("password");
            
            // Verify credentials
            String storedPassword = userStore.get(email);
            if (storedPassword == null || !storedPassword.equals(password)) {
                log.error("Invalid credentials for user: {}", email);
                return ResponseEntity.status(403).build();
            }
            
            // Create simple session token
            String sessionToken = UUID.randomUUID().toString();
            String role = email.equals("admin@example.com") ? "ADMIN" : "USER";
            
            Map<String, Object> response = new HashMap<>();
            response.put("token", sessionToken);
            response.put("type", "Simple");
            response.put("userId", email.equals("admin@example.com") ? 1L : 2L);
            response.put("email", email);
            response.put("role", role);
            
            // Store session
            sessionStore.put(sessionToken, response);
            
            log.info("Simple login successful for user: {}", email);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Simple login failed", e);
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
    public ResponseEntity<Map<String, Object>> getUser(@RequestParam String token) {
        try {
            Map<String, Object> user = sessionStore.get(token);
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
        return ResponseEntity.ok("Simple auth service is working!");
    }
}
