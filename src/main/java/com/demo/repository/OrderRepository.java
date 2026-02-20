package com.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.demo.entity.Order;
import com.demo.entity.OrderStatus;
import com.demo.entity.User;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    List<Order> findByUserOrderByOrderDateDesc(User user);

    Order findByIdAndUserId(Integer id, Integer userId);

    @Query("SELECT SUM(o.totalAmount) FROM Order o WHERE o.status = 'DELIVERED'")
    Double getTotalRevenue();

    Long countByStatus(OrderStatus status);
}
