import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GenerateBCryptHash {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String adminPassword = encoder.encode("Admin123!");
        String userPassword = encoder.encode("User123!");
        
        System.out.println("Admin password hash: " + adminPassword);
        System.out.println("User password hash: " + userPassword);
        
        // Test the hash
        System.out.println("Admin password matches: " + encoder.matches("Admin123!", adminPassword));
        System.out.println("User password matches: " + encoder.matches("User123!", userPassword));
    }
}
