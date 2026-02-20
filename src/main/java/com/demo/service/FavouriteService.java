package com.demo.service;

import java.util.List;

import com.demo.entity.Favourite;
import com.demo.entity.Product;
import com.demo.entity.User;

public interface FavouriteService {

    void addToFavourite(User user, Product product);

    void removeFavourite(User user, int productId);

    List<Favourite> getFavouritesByUser(User user);
}
