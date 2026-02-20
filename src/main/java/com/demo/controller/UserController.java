package com.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.demo.DTO.UserDTO;
import com.demo.entity.Category;
import com.demo.entity.Product;
import com.demo.entity.Role;
import com.demo.entity.User;
import com.demo.service.AdminService;
import com.demo.serviceimpl.UserServiceImpl;

import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {

    @Autowired
    private UserServiceImpl service;

    @Autowired
    private AdminService adminservice;

    @ModelAttribute
    public void addUserToModel(Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        model.addAttribute("loggedInUser", user);
    }

    @GetMapping("/")
    public String index(Model m, HttpSession session) {
        m.addAttribute("products", adminservice.getallProducts());
        return "index";
    }

    @GetMapping("/search")
    public String searchProducts(@RequestParam("keyword") String keyword, Model model) {
        List<Product> results = adminservice.searchProducts(keyword);
        model.addAttribute("products", results);
        model.addAttribute("keyword", keyword);
        return "searchResults";    // ← change here
    }



    @GetMapping("/register")
    public String register(Model m) {
        m.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/adduser")
    public String addUser(@ModelAttribute User u) {
        User saved = service.addUser(u);
        if (saved != null) {
            return "redirect:login?success";
        } else {
            return "redirect:register?error";
        }
    }



    @GetMapping("/login")
    public String login(Model m) {
        m.addAttribute("loginDTO", new UserDTO());
        return "login";
    }

    @GetMapping("/oauth2-success")
    public String oauth2Success(@AuthenticationPrincipal OAuth2User principal, HttpSession session) {
        if (principal == null) return "redirect:/login?error";

        String email = principal.getAttribute("email");
        User user = service.findByEmail(email);

        // If user is null, you MUST create them, or the login fails
        if (user == null) {
            user = new User();
            user.setEmail(email);
            user.setName(principal.getAttribute("name"));
            user.setRole(Role.USER);
            user.setPassword("OAUTH_USER_PASS"); 
            service.addUser(user); // Save to your database
        }

        // Manually set the session for your templates
        session.setAttribute("loggedInUser", user);
        return "redirect:/"; 
    }

    @PostMapping("/loginUser")
    public String validate(@ModelAttribute UserDTO login, Model m, HttpSession session) {
        if ("admin@gmail.com".equals(login.getEmail()) &&
            "admin@123".equals(login.getPassword())) {
            User admin = new User();
            admin.setName("Admin");
            admin.setEmail("admin@gmail.com");
            admin.setRole(Role.ADMIN);
            session.setAttribute("loggedInAdmin", admin);
            return "redirect:/admin/dashboard";  
        }
        User user = service.findByEmail(login.getEmail());
        if (user != null && user.getPassword().equals(login.getPassword())) {
            session.setAttribute("loggedInUser", user);
            return "redirect:/";
        }
        m.addAttribute("loginDTO", login);
        m.addAttribute("error", "Invalid email or password");
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login?logout";
    }

    @GetMapping("/profile")
    public String profile(HttpSession session, Model m) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
return "redirect:/login";
}
        m.addAttribute("user", user);
        return "profile";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable int id, Model m) {
        User u = service.findById(id);
        m.addAttribute("user", u);
        return "edit_profile";
    }

    @PostMapping("/updateUser")
    public String updateProfile(@ModelAttribute User updated, HttpSession session) {
        User saved = service.updateuser(updated);
        session.setAttribute("loggedInUser", saved);
        return "redirect:/profile";
    }

    @GetMapping("/category/Men")
    public String mensPage(Model m, HttpSession session) {
        List<Product> allproducts = adminservice.getProductsByCategory(Category.MEN);
        m.addAttribute("products", allproducts);
        return "Men";
    }



    @GetMapping("/category/Women")
    public String womenPage(Model m, HttpSession session) {
        List<Product> allproducts = adminservice.getProductsByCategory(Category.WOMEN);
        m.addAttribute("products", allproducts);
        return "Women";
    }



    @GetMapping("/category/Kids")
    public String kidsPage(Model m, HttpSession session) {
        List<Product> allproducts = adminservice.getProductsByCategory(Category.KIDS);
        m.addAttribute("products", allproducts);
        return "Kids";
    }



    @GetMapping("/category/{type}/{id}")
    public String productDetails(@PathVariable Category type, @PathVariable int id, Model model) {
        Product product = adminservice.getProductbyId(id);
        model.addAttribute("product", product);
        model.addAttribute("category", type);
        return "productDetails";
    }

}