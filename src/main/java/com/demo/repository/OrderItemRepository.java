package com.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
}
