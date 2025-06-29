package com.prajwal.ecommerce_api.controller;

import com.prajwal.ecommerce_api.dto.ProductDTO;
import com.prajwal.ecommerce_api.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Products", description = "Operations related to product management")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(summary = "Get paginated list of all products")
    @GetMapping
    public ResponseEntity<Page<ProductDTO>> getAllProducts(
            @PageableDefault(page = 0, size = 10) Pageable pageable) {
        Page<ProductDTO> products = productService.getAllProducts(pageable);
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "Get product by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Product found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Product not found", content = @Content)
            })
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        ProductDTO product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    @Operation(summary = "Create a new product (Admin only)")
    @ApiResponse(responseCode = "201", description = "Product created",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductDTO.class)))
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody ProductDTO productDTO) {
        ProductDTO createdProduct = productService.createProduct(productDTO);
        return  ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

    @Operation(summary = "Update existing product by ID (Admin only)")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductDTO productDTO) {
        ProductDTO updatedProduct = productService.updateProduct(id, productDTO);
        return ResponseEntity.ok(updatedProduct);
    }

    @Operation(summary = "Delete product by ID (Admin only)")
    @ApiResponse(responseCode = "204", description = "Product deleted successfully")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Search products by name")
    @GetMapping("/search")
    public ResponseEntity<List<ProductDTO>> searchByName(@RequestParam String name) {
        List<ProductDTO> products = productService.searchByName(name);
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "Filter products by category")
    @GetMapping("/filter/category")
    public ResponseEntity<List<ProductDTO>> filterByCategory(@RequestParam String category) {
        List<ProductDTO> products = productService.filterByCategory(category);
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "Filter products by price range")
    @GetMapping("/filter/price")
    public ResponseEntity<Page<ProductDTO>> filterByPriceRange(
            @PageableDefault(page = 0, size = 10) Pageable pageable,
            @RequestParam Double minPrice,
            @RequestParam Double maxPrice) {
        Page<ProductDTO> products = productService.filterByPriceRange(pageable, minPrice, maxPrice);
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "Filter products by price range and category")
    @GetMapping("/filter/price-and-category")
    public ResponseEntity<Page<ProductDTO>> filterByPriceAndCategory(
            @PageableDefault(page = 0, size = 10) Pageable pageable,
            @RequestParam Double minPrice,
            @RequestParam Double maxPrice,
            @RequestParam String category) {
        Page<ProductDTO> products = productService.filterByPriceAndCategory(pageable, minPrice, maxPrice, category);
        return ResponseEntity.ok(products);
    }
}
