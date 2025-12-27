package com.inventory.controller;

import com.inventory.service.InventoryService;
import com.inventory.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/inventory")
public class InventoryController {
    
    @Autowired
    private InventoryService inventoryService;
    
    @Autowired
    private ProductService productService;
    
    // Show all inventory
    @GetMapping
    public String getAllInventory(Model model) {
        model.addAttribute("inventoryList", inventoryService.getAllInventory());
        
        // Add low stock warning
        Map<Long, Boolean> lowStockMap = new HashMap<>();
        inventoryService.getLowStockItems(10.0).forEach(inv -> 
            lowStockMap.put(inv.getProduct().getId(), true));
        model.addAttribute("lowStockMap", lowStockMap);
        
        return "inventory";
    }
    
    // Show stock update form
    @GetMapping("/update/{productId}")
    public String showStockUpdateForm(@PathVariable Long productId, Model model) {
        model.addAttribute("productId", productId);
        model.addAttribute("product", productService.getProductById(productId).orElse(null));
        return "stock-update";
    }
    
    // Update stock
    @PostMapping("/update/{productId}")
    public String updateStock(@PathVariable Long productId,
                            @RequestParam String movementType,
                            @RequestParam Double quantity,
                            @RequestParam(required = false) String notes,
                            Model model) {
        try {
            inventoryService.updateStock(productId, movementType, quantity, notes);
            return "redirect:/inventory?updated";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("productId", productId);
            model.addAttribute("product", productService.getProductById(productId).orElse(null));
            return "stock-update";
        }
    }
    
    // Show stock history (optional)
    @GetMapping("/history/{productId}")
    public String showStockHistory(@PathVariable Long productId, Model model) {
        model.addAttribute("product", productService.getProductById(productId).orElse(null));
        model.addAttribute("movements", inventoryService.getStockHistory(productId));
        return "stock-history";
    }
}