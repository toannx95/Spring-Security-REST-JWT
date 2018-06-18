package com.spring.security.service;

import java.util.List;

import com.spring.security.entity.User;

public interface UserService {

	List<User> getAllUsers();

	User getUser(Long id);
	
	User getUser(String username);

}
