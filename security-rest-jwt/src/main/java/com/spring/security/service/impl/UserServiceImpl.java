package com.spring.security.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.security.entity.User;
import com.spring.security.repository.UserRepository;
import com.spring.security.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;

	@Override
	public List<User> getAlls() {
		return userRepository.findAll();
	}

}
