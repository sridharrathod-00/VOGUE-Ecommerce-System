package com.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.demo.entity.Cart;
import com.demo.entity.CartItemEntity;
import com.demo.entity.PaymentMethod;
import com.demo.entity.Product;
import com.demo.entity.User;
import com.demo.service.AdminService;
import com.demo.service.CartService;
import com.demo.service.OrderService;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private AdminService adminservice;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CartService cartService;

    @PostMapping("/add/{id}")
    public String addToCart(@PathVariable int id, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";

        Product product = adminservice.getProductbyId(id);
        if (product == null) return "redirect:/";
        
        cartService.addToCart(user, product);
        return "redirect:/cart/view";
    }

    @GetMapping("/view")
    public String viewCart(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";

        Cart cart = cartService.getCartByUser(user);
        double total = 0;

        if (cart != null && cart.getItems() != null) {
            total = cart.getItems().stream()
                    .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                    .sum();
        }

        model.addAttribute("cart", cart);
        model.addAttribute("total", total);
        return "cart";
    }

    @GetMapping("/checkout")
    public String checkout(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";

        Cart cart = cartService.getCartByUser(user);
        if (cart == null || cart.getItems().isEmpty()) return "redirect:/cart/view";

        double total = cart.getItems().stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();
                
        model.addAttribute("cart", cart);
        model.addAttribute("total", total);
        return "checkout";
    }

    @PostMapping("/place-order")
    public String placeOrder(
            @RequestParam String fullName,
            @RequestParam String address,
            @RequestParam String city,
            @RequestParam String zip,
            @RequestParam String payment,
            HttpSession session) {

        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";

        Cart cart = cartService.getCartByUser(user);
        if (cart == null || cart.getItems().isEmpty()) return "redirect:/cart/view";

        PaymentMethod method = PaymentMethod.valueOf(payment.toUpperCase());
        
        String status = "PENDING";

        orderService.placeOrder(user, cart, fullName, address, city, zip, method, status);

 
        String referenceId = "VOGUE-" + System.currentTimeMillis();

        return "redirect:/cart/order-success?id=" + referenceId;
    }

    @PostMapping("/buy-now/{id}")
    public String buyNow(@PathVariable int id, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";

        Product product = adminservice.getProductbyId(id);
        if (product != null) {
            cartService.addToCart(user, product);
        }
        return "redirect:/cart/checkout";
    }

    @GetMapping("/order-success")
    public String orderSuccess(@RequestParam(required = false) String id, Model model) {
        model.addAttribute("referenceId", id);
        return "orderSuccess";
    }

    @GetMapping("/increase/{id}")
    public String increaseQuantity(@PathVariable int id, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";

        cartService.increaseQuantity(user, id);
        return "redirect:/cart/view";
    }

    @GetMapping("/decrease/{id}")
    public String decreaseQuantity(@PathVariable int id, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";

        cartService.decreaseQuantity(user, id);
        return "redirect:/cart/view";
    }

    @GetMapping("/remove/{id}")
    public String removeFromCart(@PathVariable int id, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";

        cartService.removeItem(user, id);
        return "redirect:/cart/view";
    }

    @GetMapping("/clear")
    public String clearCart(HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";

        cartService.clearCart(user);
        return "redirect:/cart/view";
    }

    @ModelAttribute("cartCount")
    public int cartCount(HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return 0;

        Cart cart = cartService.getCartByUser(user);
        if (cart == null || cart.getItems() == null) return 0;

        return cart.getItems().stream().mapToInt(CartItemEntity::getQuantity).sum();
    }
}