package com.gestion_produit.user_service.controller;

import com.gestion_produit.user_service.entity.User;
import com.gestion_produit.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class SimpleAuthController {

    private final UserRepository userRepository;

    @PostMapping("/simple-login")
    public ResponseEntity<Map<String, Object>> simpleLogin(@RequestBody Map<String, String> loginRequest) {
        String email = loginRequest.get("email");
        String password = loginRequest.get("password");
        
        log.info("Simple login attempt for email: {}", email);
        
        try {
            Optional<User> userOpt = userRepository.findByEmail(email);
            
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                
                // Vérification simple du mot de passe (en clair pour l'instant)
                if (password.equals("Admin123!") && email.equals("admin@example.com")) {
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", true);
                    response.put("message", "Login successful");
                    response.put("userId", user.getId());
                    response.put("email", user.getEmail());
                    response.put("role", user.getRole().name());
                    response.put("token", "simple-token-" + user.getId()); // Token simple
                    
                    log.info("Login successful for email: {}", email);
                    return ResponseEntity.ok(response);
                } else if (password.equals("User123!") && !email.equals("admin@example.com")) {
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", true);
                    response.put("message", "Login successful");
                    response.put("userId", user.getId());
                    response.put("email", user.getEmail());
                    response.put("role", user.getRole().name());
                    response.put("token", "simple-token-" + user.getId()); // Token simple
                    
                    log.info("Login successful for email: {}", email);
                    return ResponseEntity.ok(response);
                } else {
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", false);
                    response.put("message", "Invalid password");
                    
                    log.warn("Login failed for email: {} - wrong password", email);
                    return ResponseEntity.badRequest().body(response);
                }
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "User not found");
                
                log.warn("Login failed for email: {} - user not found", email);
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            log.error("Login error for email: {}", email, e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Login error: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @GetMapping("/check")
    public ResponseEntity<Map<String, Object>> checkAuth(@RequestParam String token) {
        try {
            if (token != null && token.startsWith("simple-token-")) {
                Long userId = Long.parseLong(token.replace("simple-token-", ""));
                
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Token valid");
                response.put("userId", userId);
                
                return ResponseEntity.ok(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Invalid token");
                
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Token validation error");
            
            return ResponseEntity.badRequest().body(response);
        }
    }
}
