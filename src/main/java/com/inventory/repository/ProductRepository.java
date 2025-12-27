package com.inventory.repository;

import com.inventory.model.ChemicalProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ChemicalProduct, Long> {
    Optional<ChemicalProduct> findByCasNumber(String casNumber);
    boolean existsByCasNumber(String casNumber);
    List<ChemicalProduct> findByProductNameContainingIgnoreCase(String name);
}