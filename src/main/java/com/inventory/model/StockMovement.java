package com.inventory.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "stock_movements")
public class StockMovement {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private ChemicalProduct product;
    
    @Column(nullable = false)
    private String movementType;  // "IN" or "OUT"
    
    @Column(nullable = false)
    private Double quantity;
    
    @Column(nullable = false)
    private LocalDateTime movementDate = LocalDateTime.now();
    
    private String notes;
    
    // Constructors
    public StockMovement() {}
    
    public StockMovement(ChemicalProduct product, String movementType, Double quantity, String notes) {
        this.product = product;
        this.movementType = movementType;
        this.quantity = quantity;
        this.notes = notes;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public ChemicalProduct getProduct() { return product; }
    public void setProduct(ChemicalProduct product) { this.product = product; }
    
    public String getMovementType() { return movementType; }
    public void setMovementType(String movementType) { this.movementType = movementType; }
    
    public Double getQuantity() { return quantity; }
    public void setQuantity(Double quantity) { this.quantity = quantity; }
    
    public LocalDateTime getMovementDate() { return movementDate; }
    public void setMovementDate(LocalDateTime movementDate) { this.movementDate = movementDate; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}