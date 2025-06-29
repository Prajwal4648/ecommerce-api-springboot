package com.prajwal.ecommerce_api.service;

import com.prajwal.ecommerce_api.dto.ProductDTO;
import com.prajwal.ecommerce_api.exception.InvalidFilterException;
import com.prajwal.ecommerce_api.exception.ProductNotFoundException;
import com.prajwal.ecommerce_api.model.Product;
import com.prajwal.ecommerce_api.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    private final ModelMapper modelMapper;
    private final ProductRepository productRepository;

    public ProductServiceImpl(ModelMapper modelMapper, ProductRepository productRepository) {
        this.modelMapper = modelMapper;
        this.productRepository = productRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductDTO> getAllProducts(Pageable pageable) {
        return productRepository.findByActiveTrue(pageable)
                .map(product -> modelMapper.map(product, ProductDTO.class));
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .filter(Product::isActive)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
        return modelMapper.map(product, ProductDTO.class);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public ProductDTO createProduct(ProductDTO productDTO) {
        Product product = modelMapper.map(productDTO, Product.class);
        product = productRepository.save(product);
        return modelMapper.map(product, ProductDTO.class);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
        Product product = productRepository.findById(id)
                .filter(Product::isActive)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));

        if (productDTO.getName() != null) {
            product.setName(productDTO.getName());
        }
        if (productDTO.getDescription() != null) {
            product.setDescription(productDTO.getDescription());
        }
        if (productDTO.getPrice() != null) {
            product.setPrice(productDTO.getPrice());
        }
        if (productDTO.getStock() != null) {
            product.setStock(productDTO.getStock());
        }
        if (productDTO.getCategory() != null) {
            product.setCategory(productDTO.getCategory());
        }
        product = productRepository.save(product);
        return modelMapper.map(product, ProductDTO.class);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .filter(Product::isActive)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
        product.setActive(false);
        productRepository.save(product);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDTO> searchByName(String name) {
        List<Product> products = productRepository.findByNameContainingIgnoreCaseAndActiveTrue(name);
        return products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDTO> filterByCategory(String category) {
        List<Product> products = productRepository.findByCategoryAndActiveTrue(category);
        return products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductDTO> filterByPriceRange(Pageable pageable, Double minPrice, Double maxPrice) {
        validatePriceRange(minPrice, maxPrice);
        return productRepository.findByPriceBetweenAndActiveTrue(minPrice, maxPrice, pageable)
                .map(product -> modelMapper.map(product, ProductDTO.class));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductDTO> filterByPriceAndCategory(Pageable pageable, Double minPrice, Double maxPrice, String category) {
        validatePriceRange(minPrice, maxPrice);
        if (!StringUtils.hasText(category)) {
            throw new InvalidFilterException("Category cannot be blank");
        }
        return productRepository.findByPriceBetweenAndCategoryAndActiveTrue(minPrice, maxPrice, category.trim(), pageable)
                .map(product -> modelMapper.map(product, ProductDTO.class));
    }

    private void validatePriceRange(Double minPrice, Double maxPrice) {
        if (minPrice == null || maxPrice == null) {
            throw new InvalidFilterException("Minimum and maximum price must be specified");
        }
        if (minPrice < 0 || maxPrice < 0) {
            throw new InvalidFilterException("Prices cannot be negative");
        }
        if (minPrice > maxPrice) {
            throw new InvalidFilterException("Minimum price cannot exceed maximum price");
        }
    }
}