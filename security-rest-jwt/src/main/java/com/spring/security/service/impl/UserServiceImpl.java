package com.spring.security.service.impl;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.security.entity.User;
import com.spring.security.exception.NotFoundException;
import com.spring.security.repository.UserRepository;
import com.spring.security.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;

	@Override
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	@Override
	public User getUser(Long id) {
		User user = userRepository.findOne(id);
		if (Objects.isNull(user)) {
			throw new NotFoundException("User", "id", id);
		}
		return user;
	}

	@Override
	public User getUser(String username) {
		User user = userRepository.findByUsername(username);
		if (Objects.isNull(user)) {
			throw new NotFoundException("User", "username", username);
		}
		return user;
	}

}
