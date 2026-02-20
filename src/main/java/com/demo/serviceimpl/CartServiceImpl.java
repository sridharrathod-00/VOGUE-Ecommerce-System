package com.demo.serviceimpl;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.demo.entity.Cart;
import com.demo.entity.CartItemEntity;
import com.demo.entity.Product;
import com.demo.entity.User;
import com.demo.repository.CartItemRepository;
import com.demo.repository.CartRepository;
import com.demo.service.CartService;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Transactional
    @Override
    public void addToCart(User user, Product product) {

        Cart cart = cartRepository.findByUser(user);

        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
            cart.setItems(new ArrayList<>());
            cart = cartRepository.save(cart);
        }

        if (cart.getItems() != null) {
            for (CartItemEntity item : cart.getItems()) {
                if (item.getProduct().getId() == product.getId()) {
                    item.setQuantity(item.getQuantity() + 1);
                    cartItemRepository.save(item);
                    return;
                }
            }
        }

        // Add new item
        CartItemEntity newItem = new CartItemEntity();
        newItem.setCart(cart);
        newItem.setProduct(product);
        newItem.setQuantity(1);

        cartItemRepository.save(newItem);
    }

    @Override
    public Cart getCartByUser(User user) {
        return cartRepository.findByUser(user);
    }


    @Transactional
    @Override
    public void increaseQuantity(User user, int productId) {

        Cart cart = cartRepository.findByUser(user);
        if (cart == null || cart.getItems() == null) {
			return;
		}

        for (CartItemEntity item : cart.getItems()) {
            if (item.getProduct().getId() == productId) {
                item.setQuantity(item.getQuantity() + 1);
                cartItemRepository.save(item);
                return;
            }
        }
    }
    @Transactional
    @Override
    public void decreaseQuantity(User user, int productId) {

        Cart cart = cartRepository.findByUser(user);
        if (cart == null || cart.getItems() == null) {
			return;
		}

        for (CartItemEntity item : cart.getItems()) {
            if (item.getProduct().getId() == productId) {

                if (item.getQuantity() > 1) {
                    item.setQuantity(item.getQuantity() - 1);
                    cartItemRepository.save(item);
                } else {
                    cartItemRepository.delete(item);
                }
                return;
            }
        }
    }
    @Transactional
    @Override
    public void removeItem(User user, int productId) {
        Cart cart = cartRepository.findByUser(user);
        if (cart != null && cart.getItems() != null) {
            // Just remove from the list. 
            // orphanRemoval = true will handle the database DELETE automatically.
            cart.getItems().removeIf(item -> item.getProduct().getId() == productId);
            cartRepository.save(cart);
        }
    }

    @Transactional
    @Override
    public void clearCart(User user) {
        Cart cart = cartRepository.findByUser(user);
        if (cart != null && cart.getItems() != null) {
            // Clearing the list triggers a delete for all 'orphaned' items.
            cart.getItems().clear();
            cartRepository.save(cart);
        }
    }
}
