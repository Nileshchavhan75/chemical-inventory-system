package com.inventory.repository;

import com.inventory.model.Inventory;
import com.inventory.model.ChemicalProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Optional<Inventory> findByProductId(Long productId);
    Optional<Inventory> findByProduct(ChemicalProduct product);
}