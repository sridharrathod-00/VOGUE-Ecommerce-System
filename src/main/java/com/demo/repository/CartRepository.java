package com.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.entity.Cart;
import com.demo.entity.User;

public interface CartRepository extends JpaRepository<Cart, Integer> {

    Cart findByUser(User user);

}
