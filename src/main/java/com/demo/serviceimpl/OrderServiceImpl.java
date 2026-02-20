package com.demo.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.demo.entity.Cart;
import com.demo.entity.Order;
import com.demo.entity.OrderItem;
import com.demo.entity.OrderStatus;
import com.demo.entity.PaymentMethod;
import com.demo.entity.User;
import com.demo.repository.CartRepository;
import com.demo.repository.OrderRepository;
import com.demo.service.EmailService;
import com.demo.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private EmailService emailService;

	@Transactional
    @Override
    public void placeOrder(User user,
                           Cart cart,
                           String fullName,
                           String address,
                           String city,
                           String zip,
                           PaymentMethod paymentMethod,
                           String paymentStatus) {

        Order order = new Order();

        // 👤 User Association
        order.setUser(user);

        // 🚚 Shipping Details
        order.setFullName(fullName);
        order.setAddress(address);
        order.setCity(city);
        order.setZip(zip);

        // 💳 Payment Details
        order.setPaymentMethod(paymentMethod);
        order.setPaymentStatus(paymentStatus);
        
        // Default status for new orders
        order.setStatus(OrderStatus.PLACED);

        // 💰 Calculate Total Amount
        double total = cart.getItems().stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();
        order.setTotalAmount(total);

        // 📦 Convert CartItems → OrderItems
        List<OrderItem> orderItems = cart.getItems().stream().map(cartItem -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getProduct().getPrice());
            orderItem.setOrder(order);
            return orderItem;
        }).toList();

        order.setItems(orderItems);

        // 💾 Persist Order
        orderRepository.save(order);

        // 📧 TRIGGER EMAIL HERE
        // We use a timestamp for the reference ID
        String referenceId = "VOGUE-" + System.currentTimeMillis();
        emailService.sendOrderConfirmation(user.getEmail(), fullName, referenceId, total);

        // 🧹 Clear Cart After Successful Order
        cart.getItems().clear();
        cartRepository.save(cart);
    }




    @Override
    public List<Order> getOrdersByUser(User user) {
        return orderRepository.findByUserOrderByOrderDateDesc(user);
    }


    @Override
	public Order getOrderById(Integer id, User user) {
        return orderRepository.findByIdAndUserId(id, user.getId());
    }

    @Override
    @Transactional
    public void cancelOrder(Integer id, User user) {

        Order order = orderRepository.findByIdAndUserId(id, user.getId());

        if ((order == null) || (order.getStatus() != OrderStatus.PLACED)) {
            return;  // ❌ Do not allow cancel
        }

        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
    }
    @Override
    public Order getOrderById(Integer id) {
        return orderRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void updateOrderStatus(Integer orderId, OrderStatus newStatus) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        OrderStatus currentStatus = order.getStatus();

        // ❌ If already final state
        if (currentStatus == OrderStatus.DELIVERED ||
            currentStatus == OrderStatus.CANCELLED) {

            throw new RuntimeException("Order already completed. Status cannot be changed.");
        }

        // ❌ Invalid transitions
        if ((currentStatus == OrderStatus.PLACED &&
                !(newStatus == OrderStatus.SHIPPED ||
                  newStatus == OrderStatus.CANCELLED)) || (currentStatus == OrderStatus.SHIPPED &&
                newStatus != OrderStatus.DELIVERED)) {

            throw new RuntimeException("Invalid status change.");
        }

        order.setStatus(newStatus);
        orderRepository.save(order);
    }

    @Override
    public Double getTotalRevenue() {
        Double revenue = orderRepository.getTotalRevenue();
        return revenue != null ? revenue : 0;
    }

    @Override
    public Long getTotalOrders() {
        return orderRepository.count();
    }

    @Override
    public Long getDeliveredOrders() {
        return orderRepository.countByStatus(OrderStatus.DELIVERED);
    }








}
