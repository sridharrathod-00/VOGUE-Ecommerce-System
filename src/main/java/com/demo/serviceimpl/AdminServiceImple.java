package com.demo.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.entity.Category;
import com.demo.entity.Product;
import com.demo.repository.ProductRepo;
import com.demo.service.AdminService;

@Service
public class AdminServiceImple implements AdminService{

	@Autowired
	private ProductRepo repo;

	@Override
	public Product addProduct(Product p) {
		Product prod = repo.save(p);
		return prod;
	}

	@Override
	public List<Product> getallProducts() {

		List<Product> allProduct = repo.findAll();

		return allProduct;
	}


	@Override
	public Product getProductbyId(int id) {

		Product prod =  repo.findById(id).orElse(null);
		return prod;

	}



	@Override
	public Product updateProduct(Product p, int id) {

		Product prod = repo.findById(id).orElse(null);

		if(prod != null) {
			prod.setName(p.getName());
			prod.setDescription(p.getDescription());
			prod.setPrice(p.getPrice());
			prod.setQuantity(p.getQuantity());
			prod.setCategory(p.getCategory());
			prod.setImage(p.getImage());
		}

		return prod;

	}

	@Override
	public void deleteProduct(int id) {

		Product prod =  repo.findById(id).orElse(null);

		if(prod != null) {
			repo.deleteById(id);
		}

	}

	@Override
	public List<Product> getProductsByCategory(Category category) {
		return repo.findByCategory(category);
	}

	@Override
	public List<Product> searchProducts(String keyword) {
	    return repo.searchAll(keyword);
	}






}
