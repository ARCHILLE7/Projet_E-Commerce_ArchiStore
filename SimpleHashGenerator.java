import java.security.SecureRandom;
import java.util.Base64;

public class SimpleHashGenerator {
    public static void main(String[] args) {
        // Generate a simple BCrypt-like hash for testing
        String adminPassword = generateSimpleHash("Admin123!");
        String userPassword = generateSimpleHash("User123!");
        
        System.out.println("Admin password hash: " + adminPassword);
        System.out.println("User password hash: " + userPassword);
    }
    
    public static String generateSimpleHash(String password) {
        // Simple hash generation for testing purposes
        // In production, use proper BCrypt
        String salt = "$2a$10$N.zmdr9k7uOCQb376NoUnu";
        String combined = salt + password;
        return salt + Base64.getEncoder().encodeToString(combined.getBytes()).substring(0, 22);
    }
}
