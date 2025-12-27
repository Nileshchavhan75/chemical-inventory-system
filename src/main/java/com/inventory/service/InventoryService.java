package com.inventory.service;

import com.inventory.model.Inventory;
import com.inventory.model.StockMovement;
import com.inventory.repository.InventoryRepository;
import com.inventory.repository.StockMovementRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InventoryService {
    
    @Autowired
    private InventoryRepository inventoryRepository;
    
    @Autowired
    private StockMovementRepository stockMovementRepository;
    
    // Get all inventory items
    public List<Inventory> getAllInventory() {
        return inventoryRepository.findAll();
    }
    
    // Get inventory by product ID
    public Inventory getInventoryByProductId(Long productId) {
        Optional<Inventory> inventoryOpt = inventoryRepository.findByProductId(productId);
        if (inventoryOpt.isPresent()) {
            return inventoryOpt.get();
        } else {
            throw new RuntimeException("Inventory not found for product id: " + productId);
        }
    }
    
    // Update stock (IN or OUT)
    @Transactional
    public Inventory updateStock(Long productId, String movementType, Double quantity, String notes) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        
        Inventory inventory = getInventoryByProductId(productId);
        
        if (movementType.equalsIgnoreCase("IN")) {
            // Increase stock
            inventory.setCurrentStock(inventory.getCurrentStock() + quantity);
        } else if (movementType.equalsIgnoreCase("OUT")) {
            // Decrease stock (check if enough stock)
            if (inventory.getCurrentStock() < quantity) {
                throw new IllegalStateException("Insufficient stock. Available: " + inventory.getCurrentStock());
            }
            inventory.setCurrentStock(inventory.getCurrentStock() - quantity);
        } else {
            throw new IllegalArgumentException("Movement type must be IN or OUT");
        }
        
        // Save inventory
        Inventory updatedInventory = inventoryRepository.save(inventory);
        
        // Record stock movement (optional)
        StockMovement movement = new StockMovement();
        movement.setProduct(inventory.getProduct());
        movement.setMovementType(movementType.toUpperCase());
        movement.setQuantity(quantity);
        movement.setNotes(notes);
        movement.setMovementDate(LocalDateTime.now());
        stockMovementRepository.save(movement);
        
        return updatedInventory;
    }
    
    // Get low stock items (less than threshold)
    public List<Inventory> getLowStockItems(Double threshold) {
        return inventoryRepository.findAll().stream()
            .filter(inv -> inv.getCurrentStock() < threshold)
            .collect(Collectors.toList());
    }
    
    // Get stock movement history for a product
    public List<StockMovement> getStockHistory(Long productId) {
        return stockMovementRepository.findByProductIdOrderByMovementDateDesc(productId);
    }
}