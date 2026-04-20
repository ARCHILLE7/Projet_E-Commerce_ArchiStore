import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHashGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        // Hash pour Admin123!
        String adminPassword = encoder.encode("Admin123!");
        System.out.println("Admin password hash: " + adminPassword);
        
        // Hash pour User123!
        String userPassword = encoder.encode("User123!");
        System.out.println("User password hash: " + userPassword);
    }
}
