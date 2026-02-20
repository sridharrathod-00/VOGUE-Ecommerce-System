package com.demo.controller;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.demo.DTO.ProductDTO;
import com.demo.entity.Order;
import com.demo.entity.OrderStatus;
import com.demo.entity.Product;
import com.demo.entity.Role;
import com.demo.entity.User;
import com.demo.service.AdminService;
import com.demo.service.OrderService;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController {

	 @Autowired
	    private AdminService service;


	    @Autowired
	    private OrderService orderService;


	    @GetMapping("/dashboard")
	    public String dashboard(HttpSession session, Model model) {
	        checkAdmin(session);
	        model.addAttribute("totalRevenue", orderService.getTotalRevenue());
	        model.addAttribute("totalOrders", orderService.getTotalOrders());
	        model.addAttribute("deliveredOrders", orderService.getDeliveredOrders());
	        return "AdminDashboard";
	    }



	    @GetMapping("/add-product")
	    public String addProductPage(HttpSession session) {
	        checkAdmin(session);
	        return "add_product";
	    }


	    @PostMapping("/save-product")
	    public String saveProduct(@ModelAttribute ProductDTO dto,
	                              @RequestParam("image") MultipartFile file,
	                              HttpSession session,
	                              Model model) {
	        checkAdmin(session);


	        try {
	            if (file.isEmpty()) {
	                model.addAttribute("error", "Image is required");
	                return "add_product";
	            }

	            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
	            Path uploadPath = Paths.get("uploads/images");
	            Files.createDirectories(uploadPath);
	            Files.write(uploadPath.resolve(fileName), file.getBytes());

	            Product product = new Product();
	            product.setName(dto.getName());
	            product.setDescription(dto.getDescription());
	            product.setPrice(dto.getPrice());
	            product.setQuantity(dto.getQuantity());
	            product.setCategory(dto.getCategory());
	            product.setImage(fileName);

	            service.addProduct(product);
	            
	            return "redirect:/admin/products";

	        } catch (Exception e) {
	            e.printStackTrace();
	            model.addAttribute("error", "Something went wrong");
	            return "add_product";
	        }
	    }


	    @GetMapping("/products")
	    public String viewProducts(Model m, HttpSession session) {
	        checkAdmin(session);
	        m.addAttribute("products", service.getallProducts());
	        return "view_products";
	    }


	    @GetMapping("/delete/{id}")
	    public String delete(@PathVariable int id, HttpSession session) {
	        checkAdmin(session);
	        service.deleteProduct(id);
	        return "redirect:/admin/products";
	    }


	    @GetMapping("/update")
	    public String updateForm(Model m, HttpSession session) {
	        checkAdmin(session);
	        m.addAttribute("products", service.getallProducts());
	        return "updateForm";
	    }


	    @PostMapping("/update-product")
	    public String updateProduct(@ModelAttribute ProductDTO dto,
	                                @RequestParam("image") MultipartFile file,
	                                HttpSession session) throws IOException {
	        checkAdmin(session);

	        Product product = service.getProductbyId(dto.getId());

	        if (dto.getName() != null && !dto.getName().isEmpty()) {
	product.setName(dto.getName());
	}
	        if (dto.getDescription() != null && !dto.getDescription().isEmpty()) {
	product.setDescription(dto.getDescription());
	}
	        if (dto.getPrice() != null) {
	product.setPrice(dto.getPrice());
	}
	        if (dto.getQuantity() != null) {
	product.setQuantity(dto.getQuantity());
	}
	        if (dto.getCategory() != null) {
	product.setCategory(dto.getCategory());
	}

	        if (!file.isEmpty()) {
	            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
	            Path uploadPath = Paths.get(System.getProperty("user.dir") + "/uploads/images/");
	            if (!Files.exists(uploadPath)) {
	Files.createDirectories(uploadPath);
	}
	            Files.write(uploadPath.resolve(fileName), file.getBytes());
	            product.setImage(fileName);
	        }
	        service.addProduct(product);
	        return "redirect:/admin/products";
	    }



	    private void checkAdmin(HttpSession session) {

	        User admin = (User) session.getAttribute("loggedInAdmin");
	        if (admin == null || admin.getRole() != Role.ADMIN) {
	            throw new RuntimeException("Unauthorized");
	        }
	    }



	    @GetMapping("/orders/{id}")
	    public String viewOrder(@PathVariable Integer id, Model model) {
	        Order order = orderService.getOrderById(id);
	        model.addAttribute("order", order);
	        return "admin-order-details";
	    }


	    @PostMapping("/orders/update/{id}")
	    public String updateOrderStatus(@PathVariable Integer id,
	                                    @RequestParam String status,
	                                    RedirectAttributes redirectAttributes) {


	        try {
	            OrderStatus newStatus = OrderStatus.valueOf(status);
	            orderService.updateOrderStatus(id, newStatus);
	        } catch (Exception e) {
	            redirectAttributes.addFlashAttribute("error", e.getMessage());
	        }

	        return "redirect:/admin/orders/" + id;

	    }







	}
