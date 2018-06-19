package com.spring.security.service.impl;

import java.util.Collections;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.spring.security.config.JwtTokenProvider;
import com.spring.security.dto.LoginRequest;
import com.spring.security.dto.SignUpRequest;
import com.spring.security.entity.Role;
import com.spring.security.entity.User;
import com.spring.security.enumeration.RoleEnum;
import com.spring.security.exception.AppException;
import com.spring.security.exception.BadRequestException;
import com.spring.security.repository.RoleRepository;
import com.spring.security.repository.UserRepository;
import com.spring.security.service.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JwtTokenProvider tokenProvider;

	@Override
	public String authenticateUser(LoginRequest loginRequest) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		return tokenProvider.generateToken(authentication);
	}

	@Override
	public User registerUser(SignUpRequest signUpRequest) {
		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			throw new BadRequestException("Username is already taken!");
		}

		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			throw new BadRequestException("Email already in use!");
		}

		// Creating user's account
		User user = new User(signUpRequest.getUsername(), signUpRequest.getPassword(), signUpRequest.getEmail());
		user.setPassword(passwordEncoder.encode(user.getPassword()));

		Role memberRole = roleRepository.findByName(RoleEnum.MEMBER.getRole());
		if (Objects.isNull(memberRole)) {
			throw new AppException("Member Role not set.");
		}
		user.setRoles(Collections.singleton(memberRole));

		return userRepository.save(user);
	}

}
