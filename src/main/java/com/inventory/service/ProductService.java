package com.inventory.service;

import com.inventory.model.ChemicalProduct;
import com.inventory.model.Inventory;
import com.inventory.repository.ProductRepository;
import com.inventory.repository.InventoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private InventoryRepository inventoryRepository;
    
    // Create product with inventory
    @Transactional
    public ChemicalProduct createProduct(ChemicalProduct product) {
        // Check if CAS number already exists
        if (productRepository.existsByCasNumber(product.getCasNumber())) {
            throw new IllegalArgumentException("CAS number already exists: " + product.getCasNumber());
        }
        
        // Validate unit
        if (!isValidUnit(product.getUnit())) {
            throw new IllegalArgumentException("Invalid unit. Must be KG, MT, or Litre");
        }
        
        ChemicalProduct savedProduct = productRepository.save(product);
        
        // Create inventory entry for this product
        Inventory inventory = new Inventory(savedProduct);
        inventoryRepository.save(inventory);
        
        return savedProduct;
    }
    
    // Get all products
    public List<ChemicalProduct> getAllProducts() {
        return productRepository.findAll();
    }
    
    // Get product by ID
    public Optional<ChemicalProduct> getProductById(Long id) {
        return productRepository.findById(id);
    }
    
    // Update product
    @Transactional
    public ChemicalProduct updateProduct(Long id, ChemicalProduct productDetails) {
        ChemicalProduct product = productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        
        // Check if CAS number is being changed and if it already exists
        if (!product.getCasNumber().equals(productDetails.getCasNumber()) 
                && productRepository.existsByCasNumber(productDetails.getCasNumber())) {
            throw new IllegalArgumentException("CAS number already exists: " + productDetails.getCasNumber());
        }
        
        product.setProductName(productDetails.getProductName());
        product.setCasNumber(productDetails.getCasNumber());
        product.setUnit(productDetails.getUnit());
        
        return productRepository.save(product);
    }
    
    // Delete product
    @Transactional
    public void deleteProduct(Long id) {
        // First check if product has stock
        Optional<Inventory> inventoryOpt = inventoryRepository.findByProductId(id);
        
        if (inventoryOpt.isPresent()) {
            Inventory inventory = inventoryOpt.get();
            if (inventory.getCurrentStock() > 0) {
                throw new IllegalStateException("Cannot delete product with existing stock. Clear stock first.");
            }
            // Delete inventory first (due to foreign key constraint)
            inventoryRepository.delete(inventory);
        }
        
        // Then delete product
        productRepository.deleteById(id);
    }
    
    // Search products
    public List<ChemicalProduct> searchProducts(String keyword) {
        return productRepository.findByProductNameContainingIgnoreCase(keyword);
    }
    
    // Helper method to validate unit
    private boolean isValidUnit(String unit) {
        String upperUnit = unit.toUpperCase();
        return upperUnit.equals("KG") || 
               upperUnit.equals("MT") || 
               upperUnit.equals("LITRE");
    }
}