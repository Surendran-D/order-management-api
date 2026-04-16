package com.services.order_management.controller;

import com.services.order_management.entity.Products;
import com.services.order_management.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
//@RequiredArgsConstructor
public class ProductController {

    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @PostMapping
    public Products addProduct(@RequestBody Products product) {
        return productRepository.save(product);
    }

    @GetMapping
    public List<Products> getAllProducts() {
        return productRepository.findAll();
    }

    @GetMapping("/category/{category}")
    public List<Products> getByCategory(@PathVariable String category) {
        return productRepository.findByCategory(category);
    }
}
