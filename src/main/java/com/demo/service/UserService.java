package com.demo.service;

import java.util.List;

import com.demo.entity.User;

public interface UserService {


	public User addUser(User u);
	public User updateuser(User u);
	public void deleteUser(int id);
	public List<User> allUser();
	public User findById(int id);
	public User findByEmail(String email);


}
