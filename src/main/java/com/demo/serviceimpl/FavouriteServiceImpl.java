package com.demo.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.entity.Favourite;
import com.demo.entity.Product;
import com.demo.entity.User;
import com.demo.repository.FavouriteRepository;
import com.demo.repository.ProductRepo;
import com.demo.service.FavouriteService;

@Service
public class FavouriteServiceImpl implements FavouriteService {

    @Autowired
    private FavouriteRepository favouriteRepository;

    @Autowired
    private ProductRepo productRepository;

    @Override
    public void addToFavourite(User user, Product product) {

        Favourite existing =
                favouriteRepository.findByUserAndProduct(user, product);

        if (existing == null) {
            Favourite favourite = new Favourite();
            favourite.setUser(user);
            favourite.setProduct(product);
            favouriteRepository.save(favourite);
        }
    }

    @Override
    public void removeFavourite(User user, int productId) {

        Product product = productRepository.findById(productId).orElse(null);
        if (product == null) {
			return;
		}

        Favourite favourite =
                favouriteRepository.findByUserAndProduct(user, product);

        if (favourite != null) {
            favouriteRepository.delete(favourite);
        }
    }

    @Override
    public List<Favourite> getFavouritesByUser(User user) {
        return favouriteRepository.findByUser(user);
    }
}
