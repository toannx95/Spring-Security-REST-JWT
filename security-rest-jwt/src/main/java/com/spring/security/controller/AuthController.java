package com.spring.security.controller;

import java.net.URI;
import java.util.Collections;
import java.util.Objects;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.spring.security.config.JwtTokenProvider;
import com.spring.security.dto.JwtAuthenticationResponse;
import com.spring.security.dto.LoginRequest;
import com.spring.security.dto.ApiResponse;
import com.spring.security.dto.SignUpRequest;
import com.spring.security.entity.Role;
import com.spring.security.entity.User;
import com.spring.security.enumeration.RoleEnum;
import com.spring.security.exception.AppException;
import com.spring.security.repository.RoleRepository;
import com.spring.security.repository.UserRepository;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	JwtTokenProvider tokenProvider;

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		String jwt = tokenProvider.generateToken(authentication);
		return ResponseEntity.ok().body(new JwtAuthenticationResponse(jwt));
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			return ResponseEntity.badRequest().body(new ApiResponse(false, "Username is already taken!"));
		}

		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			return ResponseEntity.badRequest().body(new ApiResponse(false, "Email already in use!"));
		}

		// Creating user's account
		User user = new User(signUpRequest.getUsername(), signUpRequest.getPassword(), signUpRequest.getEmail());
		user.setPassword(passwordEncoder.encode(user.getPassword()));

		Role memberRole = roleRepository.findByName(RoleEnum.MEMBER.getRole());
		if (Objects.isNull(memberRole)) {
			throw new AppException("Member Role not set.");
		}
		user.setRoles(Collections.singleton(memberRole));

		User result = userRepository.save(user);
		URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/users/{username}")
				.buildAndExpand(result.getUsername()).toUri();

		return ResponseEntity.created(location).body(new ApiResponse(true, "User registered successfully!"));
	}

}
