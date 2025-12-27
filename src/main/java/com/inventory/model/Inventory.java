package com.inventory.model;

import jakarta.persistence.*;

@Entity
@Table(name = "inventory")
public class Inventory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id", unique = true)
    private ChemicalProduct product;
    
    @Column(nullable = false)
    private Double currentStock = 0.0;
    
    // Constructors
    public Inventory() {}
    
    public Inventory(ChemicalProduct product) {
        this.product = product;
        this.currentStock = 0.0;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public ChemicalProduct getProduct() { return product; }  // THIS WAS MISSING!
    public void setProduct(ChemicalProduct product) { this.product = product; }
    
    public Double getCurrentStock() { return currentStock; }
    public void setCurrentStock(Double currentStock) { this.currentStock = currentStock; }
}