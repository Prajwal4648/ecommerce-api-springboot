package com.prajwal.ecommerce_api.service;

import com.prajwal.ecommerce_api.dto.ProductDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    Page<ProductDTO> getAllProducts(Pageable pageable);
    ProductDTO getProductById(Long id);
    ProductDTO createProduct(ProductDTO productDTO);
    ProductDTO updateProduct(Long id, ProductDTO productDTO);
    void deleteProduct(Long id);
    List<ProductDTO> searchByName(String name);
    List<ProductDTO> filterByCategory(String category);
    Page<ProductDTO> filterByPriceRange(Pageable pageable, Double minPrice, Double maxPrice);
    Page<ProductDTO> filterByPriceAndCategory(Pageable pageable, Double minPrice, Double maxPrice, String category);
}