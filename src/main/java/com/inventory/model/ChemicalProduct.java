package com.inventory.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "chemical_products")
public class ChemicalProduct {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Product name is required")
    @Column(nullable = false)
    private String productName;
    
    @NotBlank(message = "CAS number is required")
    @Column(unique = true, nullable = false)
    @Pattern(regexp = "^\\d{1,7}-\\d{2}-\\d$", message = "CAS number format: 000000-00-0")
    private String casNumber;
    
    @NotBlank(message = "Unit is required")
    @Column(nullable = false)
    private String unit;  // KG, MT, Litre
    
    // Constructors
    public ChemicalProduct() {}
    
    public ChemicalProduct(String productName, String casNumber, String unit) {
        this.productName = productName;
        this.casNumber = casNumber;
        this.unit = unit;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    
    public String getCasNumber() { return casNumber; }
    public void setCasNumber(String casNumber) { this.casNumber = casNumber; }
    
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
}