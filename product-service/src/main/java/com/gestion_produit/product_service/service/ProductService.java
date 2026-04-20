package com.gestion_produit.product_service.service;

import com.gestion_produit.product_service.dto.ProductDTO;
import com.gestion_produit.product_service.dto.ProductMapper;
import com.gestion_produit.product_service.entity.Product;
import com.gestion_produit.product_service.exception.ResourceNotFoundException;
import com.gestion_produit.product_service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final OrderServiceClient orderServiceClient;

    public List<ProductDTO> getAllProducts() {
        log.info("Fetching all products");
        return productRepository.findAll().stream()
                .filter(Product::getIsActive)
                .map(productMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ProductDTO getProductById(Long id) {
        log.info("Fetching product by id: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produit non trouvé avec l'id: " + id));
        
        if (!product.getIsActive()) {
            throw new ResourceNotFoundException("Produit non actif avec l'id: " + id);
        }
        
        return productMapper.toDTO(product);
    }

    public ProductDTO createProduct(ProductDTO productDTO) {
        log.info("Creating new product: {}", productDTO.getNom());
        
        Product product = productMapper.toEntity(productDTO);
        Product savedProduct = productRepository.save(product);
        
        log.info("Product created successfully with id: {}", savedProduct.getId());
        return productMapper.toDTO(savedProduct);
    }

    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
        log.info("Updating product: {}", id);
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produit non trouvé avec l'id: " + id));

        existingProduct.setNom(productDTO.getNom());
        existingProduct.setDescription(productDTO.getDescription());
        existingProduct.setPrix(productDTO.getPrix());
        existingProduct.setStock(productDTO.getStock());
        existingProduct.setImageUrl(productDTO.getImageUrl());

        Product updatedProduct = productRepository.save(existingProduct);
        log.info("Product updated successfully: {}", id);
        return productMapper.toDTO(updatedProduct);
    }

    public void deleteProduct(Long id) {
        log.info("Deleting product: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produit non trouvé avec l'id: " + id));
        
        // Check if product is used in any orders
        boolean isUsedInOrders = orderServiceClient.isProductUsedInOrders(id);
        if (isUsedInOrders) {
            // Deactivate instead of delete
            product.setIsActive(false);
            productRepository.save(product);
            log.info("Product deactivated (used in orders): {}", id);
        } else {
            productRepository.delete(product);
            log.info("Product deleted successfully: {}", id);
        }
    }

    public ProductDTO reduceStock(Long productId, Integer quantity) {
        log.info("Reducing stock for product {} by {}", productId, quantity);
        
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Produit non trouvé avec l'id: " + productId));

        if (product.getStock() < quantity) {
            throw new IllegalArgumentException("Stock insuffisant pour le produit: " + productId);
        }

        product.setStock(product.getStock() - quantity);
        Product updatedProduct = productRepository.save(product);
        
        log.info("Stock reduced successfully. New stock: {}", updatedProduct.getStock());
        return productMapper.toDTO(updatedProduct);
    }

    public ProductDTO increaseStock(Long productId, Integer quantity) {
        log.info("Increasing stock for product {} by {}", productId, quantity);
        
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Produit non trouvé avec l'id: " + productId));

        product.setStock(product.getStock() + quantity);
        Product updatedProduct = productRepository.save(product);
        
        log.info("Stock increased successfully. New stock: {}", updatedProduct.getStock());
        return productMapper.toDTO(updatedProduct);
    }

    public List<ProductDTO> getLowStockProducts(Integer threshold) {
        log.info("Fetching products with stock below threshold: {}", threshold);
        return productRepository.findByStockLessThan(threshold).stream()
                .filter(Product::getIsActive)
                .map(productMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<ProductDTO> searchProducts(String keyword) {
        log.info("Searching products with keyword: {}", keyword);
        return productRepository.findByNomContainingIgnoreCase(keyword).stream()
                .filter(Product::getIsActive)
                .map(productMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<ProductDTO> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        log.info("Fetching products in price range: {} - {}", minPrice, maxPrice);
        return productRepository.findByPrixBetween(minPrice, maxPrice).stream()
                .filter(Product::getIsActive)
                .map(productMapper::toDTO)
                .collect(Collectors.toList());
    }

    public boolean isProductAvailable(Long productId, Integer requiredQuantity) {
        log.info("Checking availability for product {} quantity {}", productId, requiredQuantity);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Produit non trouvé avec l'id: " + productId));
        
        return product.getIsActive() && product.getStock() >= requiredQuantity;
    }

    public void activateProduct(Long id) {
        log.info("Activating product: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produit non trouvé avec l'id: " + id));
        product.setIsActive(true);
        productRepository.save(product);
        log.info("Product activated successfully: {}", id);
    }

    public void deactivateProduct(Long id) {
        log.info("Deactivating product: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produit non trouvé avec l'id: " + id));
        product.setIsActive(false);
        productRepository.save(product);
        log.info("Product deactivated successfully: {}", id);
    }
}
