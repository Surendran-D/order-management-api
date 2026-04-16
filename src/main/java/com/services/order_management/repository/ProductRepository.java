package com.services.order_management.repository;

import com.services.order_management.entity.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Products, Long> {

    List<Products> findByCategory(String category);
}
