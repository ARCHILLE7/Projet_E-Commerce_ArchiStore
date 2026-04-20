package com.gestion_produit.user_service.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordHashGenerator {
    
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        // Generate hash for Admin123!
        String adminHash = encoder.encode("Admin123!");
        System.out.println("Hash for 'Admin123!': " + adminHash);
        
        // Generate hash for User123!
        String userHash = encoder.encode("User123!");
        System.out.println("Hash for 'User123!': " + userHash);
        
        // Test the hashes
        System.out.println("Admin123! matches: " + encoder.matches("Admin123!", adminHash));
        System.out.println("User123! matches: " + encoder.matches("User123!", userHash));
    }
}
