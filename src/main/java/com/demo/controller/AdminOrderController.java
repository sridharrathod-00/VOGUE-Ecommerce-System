package com.demo.controller;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.demo.entity.Order;
import com.demo.entity.OrderStatus;

import com.demo.repository.OrderRepository;


import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin/orders")
public class AdminOrderController {

	 @Autowired

	    private OrderRepository orderRepository;



	    @GetMapping

	    public String viewOrders(Model model, HttpSession session) {



	        // optional: you can reuse your checkAdmin logic later

	        List<Order> orders = orderRepository.findAll();

	        model.addAttribute("orders", orders);



	        return "admin-orders";

	    }



	    @GetMapping("/update/{id}/{status}")

	    public String updateStatus(@PathVariable Integer id,

	                               @PathVariable OrderStatus status) {



	        Order order = orderRepository.findById(id).orElse(null);



	        if (order != null) {

	            order.setStatus(status);

	            orderRepository.save(order);

	        }



	        return "redirect:/admin/orders";

	    }

	}