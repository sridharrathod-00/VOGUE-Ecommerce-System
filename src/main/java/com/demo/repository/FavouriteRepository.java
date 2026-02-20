package com.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.entity.Favourite;
import com.demo.entity.Product;
import com.demo.entity.User;

public interface FavouriteRepository extends JpaRepository<Favourite, Integer> {

    List<Favourite> findByUser(User user);

    Favourite findByUserAndProduct(User user, Product product);
}
