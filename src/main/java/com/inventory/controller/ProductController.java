package com.inventory.controller;

import com.inventory.model.ChemicalProduct;
import com.inventory.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {
    
    @Autowired
    private ProductService productService;
    
    // Show all products
    @GetMapping
    public String getAllProducts(Model model) {
        List<ChemicalProduct> products = productService.getAllProducts();
        model.addAttribute("products", products);
        model.addAttribute("product", new ChemicalProduct()); // For add form
        return "products";
    }
    
    // Show add product form
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("product", new ChemicalProduct());
        return "add-product";
    }
    
    // Add new product
    @PostMapping
    public String addProduct(@Valid @ModelAttribute("product") ChemicalProduct product, 
                           BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("products", productService.getAllProducts());
            return "products";
        }
        
        try {
            productService.createProduct(product);
            return "redirect:/products?success";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("products", productService.getAllProducts());
            return "products";
        }
    }
    
    // Show edit form
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        ChemicalProduct product = productService.getProductById(id)
            .orElseThrow(() -> new RuntimeException("Product not found"));
        model.addAttribute("product", product);
        return "edit-product";
    }
    
    // Update product
    @PostMapping("/update/{id}")
    public String updateProduct(@PathVariable Long id, 
                              @Valid @ModelAttribute("product") ChemicalProduct product,
                              BindingResult result, Model model) {
        if (result.hasErrors()) {
            product.setId(id);
            return "edit-product";
        }
        
        try {
            productService.updateProduct(id, product);
            return "redirect:/products?updated";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            product.setId(id);
            return "edit-product";
        }
    }
    
    // Delete product
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return "redirect:/products?deleted";
        } catch (Exception e) {
            return "redirect:/products?error=" + e.getMessage();
        }
    }
    
    // Search products
    @GetMapping("/search")
    public String searchProducts(@RequestParam String keyword, Model model) {
        List<ChemicalProduct> products = productService.searchProducts(keyword);
        model.addAttribute("products", products);
        model.addAttribute("searchKeyword", keyword);
        return "products";
    }
}