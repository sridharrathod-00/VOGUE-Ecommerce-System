package com.demo.service;

import com.demo.entity.Cart;
import com.demo.entity.Product;
import com.demo.entity.User;

public interface CartService {

    void addToCart(User user, Product product);

    Cart getCartByUser(User user);

    void increaseQuantity(User user, int productId);

    void decreaseQuantity(User user, int productId);

    void removeItem(User user, int productId);

    void clearCart(User user);
}
