package com.spring.security.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.spring.security.entity.User;

public interface UserService {

	Page<User> getAllUsers(Pageable pageable);

	User getUser(Long id);

	User getUser(String username);

}
