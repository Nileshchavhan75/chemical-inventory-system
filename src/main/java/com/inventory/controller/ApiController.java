package com.inventory.controller;

import com.inventory.model.ChemicalProduct;
import com.inventory.model.Inventory;
import com.inventory.service.ProductService;
import com.inventory.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = {"http://localhost:5500", "http://127.0.0.1:5500", 
        "http://localhost:3000", "http://127.0.0.1:3000"})
public class ApiController {
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private InventoryService inventoryService;
    
    // Product APIs
    @GetMapping("/products")
    public List<ChemicalProduct> getAllProducts() {
        return productService.getAllProducts();
    }
    
    @PostMapping("/products")
    public ResponseEntity<?> addProduct(@RequestBody ChemicalProduct product) {
        try {
            ChemicalProduct savedProduct = productService.createProduct(product);
            return ResponseEntity.ok(savedProduct);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @PutMapping("/products/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody ChemicalProduct product) {
        try {
            ChemicalProduct updatedProduct = productService.updateProduct(id, product);
            return ResponseEntity.ok(updatedProduct);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @DeleteMapping("/products/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok("Product deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    // Inventory APIs
    @GetMapping("/inventory")
    public List<Inventory> getAllInventory() {
        return inventoryService.getAllInventory();
    }
    
    @PostMapping("/inventory/{productId}/stock")
    public ResponseEntity<?> updateStock(@PathVariable Long productId, 
                                        @RequestParam String movementType,
                                        @RequestParam Double quantity,
                                        @RequestParam(required = false) String notes) {
        try {
            Inventory updated = inventoryService.updateStock(productId, movementType, quantity, notes);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    // Search products
    @GetMapping("/products/search")
    public List<ChemicalProduct> searchProducts(@RequestParam String keyword) {
        return productService.searchProducts(keyword);
    }
    
    // Get stock history (optional)
    @GetMapping("/inventory/{productId}/history")
    public ResponseEntity<?> getStockHistory(@PathVariable Long productId) {
        try {
            return ResponseEntity.ok(inventoryService.getStockHistory(productId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}