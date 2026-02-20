package com.demo.service;

import java.util.List;

import com.demo.entity.Cart;
import com.demo.entity.Order;
import com.demo.entity.OrderStatus;
import com.demo.entity.PaymentMethod;
import com.demo.entity.User;

public interface OrderService {

	// In OrderService.java
	void placeOrder(User user, Cart cart, String fullName, String address, 
	                String city, String zip, PaymentMethod paymentMethod, 
	                String paymentStatus);


	List<Order> getOrdersByUser(User user);

	Order getOrderById(Integer id);


	Order getOrderById(Integer id, User user);


	void cancelOrder(Integer id, User user);
	void updateOrderStatus(Integer orderId, OrderStatus newStatus);



    Double getTotalRevenue();
    Long getTotalOrders();
    Long getDeliveredOrders();





}
