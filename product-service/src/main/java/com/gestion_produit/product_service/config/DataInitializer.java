package com.gestion_produit.product_service.config;

import com.gestion_produit.product_service.entity.Product;
import com.gestion_produit.product_service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final ProductRepository productRepository;

    @Override
    public void run(String... args) throws Exception {
        if (productRepository.count() == 0) {
            log.info("Initializing default products data...");
            
            List<Product> products = Arrays.asList(
                Product.builder()
                        .nom("Laptop Dell XPS 15")
                        .description("Ordinateur portable haute performance avec écran 4K, processeur Intel Core i7, 16GB RAM, 512GB SSD. Idéal pour les professionnels et les créateurs.")
                        .prix(new BigDecimal("12999.00"))
                        .stock(25)
                        .imageUrl("https://images.unsplash.com/photo-1496181133206-80ce9b88a853?w=400")
                        .isActive(true)
                        .createdAt(LocalDateTime.now())
                        .build(),
                
                Product.builder()
                        .nom("Smartphone Samsung Galaxy S23")
                        .description("Smartphone haut de gamme avec écran AMOLED 6.1 pouces, processeur Snapdragon 8 Gen 2, 256GB stockage, appareil photo 50MP.")
                        .prix(new BigDecimal("8999.00"))
                        .stock(50)
                        .imageUrl("https://images.unsplash.com/photo-1580910051074-3eb694886505?w=400")
                        .isActive(true)
                        .createdAt(LocalDateTime.now())
                        .build(),
                
                Product.builder()
                        .nom("Casque Audio Sony WH-1000XM5")
                        .description("Casque sans fil à réduction de bruit active, qualité audio premium, autonomie 30 heures, confort optimal pour un usage prolongé.")
                        .prix(new BigDecimal("3499.00"))
                        .stock(75)
                        .imageUrl("https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=400")
                        .isActive(true)
                        .createdAt(LocalDateTime.now())
                        .build(),
                
                Product.builder()
                        .nom("Tablette iPad Pro 12.9")
                        .description("Tablette professionnelle avec écran Liquid Retina XDR 12.9 pouces, chip M2, 256GB, compatible Apple Pencil et Magic Keyboard.")
                        .prix(new BigDecimal("11999.00"))
                        .stock(15)
                        .imageUrl("https://images.unsplash.com/photo-1544244015-0df4b3ffc6b0?w=400")
                        .isActive(true)
                        .createdAt(LocalDateTime.now())
                        .build(),
                
                Product.builder()
                        .nom("Montre Connectée Apple Watch Series 8")
                        .description("Smartwatch avec suivi santé avancé, GPS, étanche jusqu'à 50m, écran Always-On, autonomie 18 heures.")
                        .prix(new BigDecimal("4599.00"))
                        .stock(40)
                        .imageUrl("https://images.unsplash.com/photo-1551816230-ef5deaed4a26?w=400")
                        .isActive(true)
                        .createdAt(LocalDateTime.now())
                        .build(),
                
                Product.builder()
                        .nom("Enceinte Bluetooth JBL Flip 6")
                        .description("Enceinte portable étanche IP67, son puissant 30W, autonomie 12 heures, connexion Bluetooth 5.1, design compact.")
                        .prix(new BigDecimal("899.00"))
                        .stock(100)
                        .imageUrl("https://images.unsplash.com/photo-1608043152269-423dbba4e7e1?w=400")
                        .isActive(true)
                        .createdAt(LocalDateTime.now())
                        .build(),
                
                Product.builder()
                        .nom("Clavier Mécanique Logitech MX Keys")
                        .description("Clavier mécanique sans fil, rétroéclairage intelligent, connexion multi-appareils, touches concaves pour confort optimal.")
                        .prix(new BigDecimal("1299.00"))
                        .stock(60)
                        .imageUrl("https://images.unsplash.com/photo-1587829741300-df743b926d2b?w=400")
                        .isActive(true)
                        .createdAt(LocalDateTime.now())
                        .build(),
                
                Product.builder()
                        .nom("Souris Gaming Logitech G502")
                        .description("Souris gaming haute précision 25,600 DPI, 11 boutons programmables, poids ajustable, éclairage RGB personnalisable.")
                        .prix(new BigDecimal("699.00"))
                        .stock(85)
                        .imageUrl("https://images.unsplash.com/photo-1615655987263-65f063f19ce2?w=400")
                        .isActive(true)
                        .createdAt(LocalDateTime.now())
                        .build(),
                
                Product.builder()
                        .nom("Moniteur Gaming LG 27GP850")
                        .description("Moniteur 27 pouces QHD 2560x1440, 165Hz, 1ms, G-Sync Compatible, HDR10, pour gaming et création.")
                        .prix(new BigDecimal("3999.00"))
                        .stock(20)
                        .imageUrl("https://images.unsplash.com/photo-1527443224622-71a89e5e3b1c?w=400")
                        .isActive(true)
                        .createdAt(LocalDateTime.now())
                        .build(),
                
                Product.builder()
                        .nom("Webcam Logitech StreamCam")
                        .description("Webcam Full HD 1080p 60fps, autofocus, champ de vision 78°, microphones stéréo, montage flexible.")
                        .prix(new BigDecimal("799.00"))
                        .stock(55)
                        .imageUrl("https://images.unsplash.com/photo-1592478411213-6153e4ebc07d?w=400")
                        .isActive(true)
                        .createdAt(LocalDateTime.now())
                        .build(),
                
                Product.builder()
                        .nom("Disque SSD externe Samsung T7")
                        .description("Disque SSD portable 1TB, vitesse de lecture/écriture jusqu'à 1,050/1,000 MB/s, connexion USB-C, sécurité par mot de passe.")
                        .prix(new BigDecimal("1499.00"))
                        .stock(45)
                        .imageUrl("https://images.unsplash.com/photo-1592478411213-6153e4ebc07d?w=400")
                        .isActive(true)
                        .createdAt(LocalDateTime.now())
                        .build(),
                
                Product.builder()
                        .nom("HUB USB-C Anker PowerExpand")
                        .description("Concentrateur USB-C 7-en-1, ports HDMI 4K, USB 3.0, SD/microSD, Ethernet 1Gbps, alimentation 100W.")
                        .prix(new BigDecimal("599.00"))
                        .stock(70)
                        .imageUrl("https://images.unsplash.com/photo-1586953208448-b95a79798f07?w=400")
                        .isActive(true)
                        .createdAt(LocalDateTime.now())
                        .build()
            );

            productRepository.saveAll(products);
            log.info("Default products initialized successfully! {} products added.", products.size());
        }
    }
}
