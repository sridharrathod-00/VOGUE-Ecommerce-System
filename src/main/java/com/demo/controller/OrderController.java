package com.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.demo.entity.Order;
import com.demo.entity.User;
import com.demo.service.OrderService;
import jakarta.servlet.http.HttpSession;

@Controller
public class OrderController {

	@Autowired

    private OrderService orderService;



    @GetMapping("/orders")
    public String viewOrders(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }
        List<Order> orders = orderService.getOrdersByUser(user);
        model.addAttribute("orders", orders);
        return "orders";
    }


    @GetMapping("/orders/{id}")
    public String orderDetails(@PathVariable Integer id,
                               HttpSession session,
                               Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }
        Order order = orderService.getOrderById(id, user);
        if (order == null) {
            return "redirect:/orders";  
        }
        model.addAttribute("order", order);
        return "order-details";

    }


    @GetMapping("/orders/cancel/{id}")
    public String cancelOrder(@PathVariable Integer id,
                              HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }
        orderService.cancelOrder(id, user);
        return "redirect:/orders";
    }







}