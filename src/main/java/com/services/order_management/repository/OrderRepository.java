package com.services.order_management.repository;


import com.services.order_management.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {

    List<Orders> findByCustomerName(String customerName);

    List<Orders> findByStatus(String status);

}
