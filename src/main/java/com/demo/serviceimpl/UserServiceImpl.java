package com.demo.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.entity.User;
import com.demo.repository.UserRepository;
import com.demo.service.UserService;
@Service
public class UserServiceImpl implements UserService{
	@Autowired
	private UserRepository repo;

	@Override
	public User addUser(User u) {
//	u.setRole(Role.USER);
	User us = repo.save(u);
		return us;
	}

	@Override
	public User updateuser(User u) {
		User us = repo.save(u);
		return us;
	}

	@Override
	public void deleteUser(int id) {
		repo.deleteById(id);

	}

	@Override
	public List<User> allUser() {
	List<User> users =	repo.findAll();
		return users;
	}

	@Override
	public User findById(int id) {
	User us =	repo.findById(id).orElse(null);
		return us;
	}

	@Override
	public User findByEmail(String email) {
	User us =	repo.findByEmail(email);
	return us;
	}

}
