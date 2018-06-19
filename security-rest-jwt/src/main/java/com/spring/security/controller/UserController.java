package com.spring.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.security.entity.User;
import com.spring.security.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

	@Autowired
	UserService userService;

	@GetMapping()
	public ResponseEntity<Page<?>> getAllUsers(Pageable pageable) {
		Page<User> users = userService.getAllUsers(pageable);
		if (users.getSize() == 0) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(users);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getUser(@PathVariable("id") Long id) {
		return ResponseEntity.ok(userService.getUser(id));
	}

	@GetMapping("/getByUsername/{username}")
	public ResponseEntity<?> getUser(@PathVariable("username") String username) {
		return ResponseEntity.ok(userService.getUser(username));
	}

}
