package com.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.demo.entity.Category;
import com.demo.entity.Product;

@Service
public interface AdminService {

	public Product addProduct(Product p);
	public List<Product> getallProducts();
	public List<Product> getProductsByCategory(Category category);
	public Product getProductbyId(int id);
	public Product updateProduct(Product p ,int id);
	public void deleteProduct(int id);
	public List<Product> searchProducts(String keyword);



}
