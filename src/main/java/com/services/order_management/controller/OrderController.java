package com.services.order_management.controller;


import com.services.order_management.entity.Orders;
import com.services.order_management.entity.Products;
import com.services.order_management.repository.OrderRepository;
import com.services.order_management.repository.ProductRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderController(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @PostMapping
    public ResponseEntity<?> placeOrder(@RequestBody Orders order) {
        // Check product exists
        Products product = productRepository.findById(order.getProductId())
                .orElse(null);

        if (product == null) {
            return ResponseEntity.badRequest().body("Product not found!");
        }

        // Check stock
        if (product.getStock() < order.getQuantity()) {
            return ResponseEntity.badRequest().body("Insufficient stock!");
        }

        // Calculate total price
        order.setTotalPrice(product.getPrice() * order.getQuantity());

        // Reduce stock
        product.setStock(product.getStock() - order.getQuantity());
        productRepository.save(product);

        // Save order
        Orders saved = orderRepository.save(order);
        return ResponseEntity.ok(saved);
    }

    // Get all orders
    @GetMapping
    public List<Orders> getAllOrders() {
        return orderRepository.findAll();
    }

    // Get order by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable Long id) {
        return orderRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Get orders by customer
    @GetMapping("/customer/{name}")
    public List<Orders> getByCustomer(@PathVariable String name) {
        return orderRepository.findByCustomerName(name);
    }

    // Cancel order
    @PutMapping("/{id}/cancel")
    public ResponseEntity<?> cancelOrder(@PathVariable Long id) {
        Orders order = orderRepository.findById(id).orElse(null);

        if (order == null) {
            return ResponseEntity.badRequest().body("Order not found!");
        }

        if (order.getStatus().equals("CANCELLED")) {
            return ResponseEntity.badRequest().body("Already cancelled!");
        }

        order.setStatus("CANCELLED");

        // Restore stock
        Products product = productRepository.findById(order.getProductId())
                .orElse(null);
        if (product != null) {
            product.setStock(product.getStock() + order.getQuantity());
            productRepository.save(product);
        }

        orderRepository.save(order);
        return ResponseEntity.ok("Order cancelled successfully!");
    }
}
