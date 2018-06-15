package com.spring.security.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.spring.security.entity.User;
import com.spring.security.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

	@Autowired
	UserService userService;

	@GetMapping()
	@ResponseBody
	public ResponseEntity<List<User>> getAllUsers() {
		List<User> users = userService.getAlls();
		if (users.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok().body(users);
	}

}
