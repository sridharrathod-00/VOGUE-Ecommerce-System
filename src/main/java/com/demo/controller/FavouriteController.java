package com.demo.controller;




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.demo.entity.Product;
import com.demo.entity.User;
import com.demo.service.AdminService;
import com.demo.service.FavouriteService;


import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/favourite")
public class FavouriteController {

	@Autowired
    private AdminService adminservice;



    @Autowired
    private FavouriteService favouriteService;



    @GetMapping("/add/{id}")
    public String addToFavourite(@PathVariable int id, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }

        Product product = adminservice.getProductbyId(id);
        if (product == null) {
            return "redirect:/";
        }
        favouriteService.addToFavourite(user, product);
        return "redirect:/favourite/view";
    }



    @GetMapping("/view")
    public String viewFavourite(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("favourites",
                favouriteService.getFavouritesByUser(user));
        return "favourite";
    }


    @GetMapping("/remove/{id}")
    public String removeFavourite(@PathVariable int id,
                                  HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }
        favouriteService.removeFavourite(user, id);
        return "redirect:/favourite/view";

    }

}