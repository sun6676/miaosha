package com.example.seckilldemo.repository;

import com.example.seckilldemo.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, String> {
}
