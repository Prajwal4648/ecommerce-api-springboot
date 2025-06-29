package com.prajwal.ecommerce_api.repository;

import com.prajwal.ecommerce_api.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategoryAndActiveTrue(String category);
    Page<Product> findByActiveTrue(Pageable pageable);
    List<Product> findByNameContainingIgnoreCaseAndActiveTrue(String name);
    Page<Product> findByPriceBetweenAndActiveTrue(Double minPrice, Double maxPrice, Pageable pageable);
    Page<Product> findByPriceBetweenAndCategoryAndActiveTrue(Double minPrice, Double maxPrice, String category, Pageable pageable);
}