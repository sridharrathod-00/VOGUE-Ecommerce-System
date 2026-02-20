package com.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.entity.Cart;
import com.demo.entity.CartItemEntity;

public interface CartItemRepository extends JpaRepository<CartItemEntity, Integer> {
	
	void deleteAllByCart(Cart cart);
	
}
