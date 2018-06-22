package com.spring.security.controller;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.spring.security.config.JwtTokenProvider;
import com.spring.security.dto.ApiResponseDto;
import com.spring.security.dto.JwtAuthResponseDto;
import com.spring.security.dto.LoginDto;
import com.spring.security.dto.PasswordDto;
import com.spring.security.dto.SignUpDto;
import com.spring.security.entity.User;
import com.spring.security.exception.BadRequestException;
import com.spring.security.service.AuthService;
import com.spring.security.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenProvider tokenProvider;

	@Autowired
	private AuthService authService;

	@Autowired
	UserService userService;

	@PostMapping("/generate-token")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginDto loginDto) {
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = tokenProvider.generateToken(authentication);
		return ResponseEntity.ok(new JwtAuthResponseDto(jwt));
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpDto signUpDto) {
		User user = authService.registerUser(signUpDto);
		URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/users/{username}")
				.buildAndExpand(user.getUsername()).toUri();
		return ResponseEntity.created(location).body(new ApiResponseDto(true, "User registered successfully!"));
	}

	@PostMapping("/password/update")
	public ResponseEntity<?> updatePassword(@RequestBody PasswordDto passwordDto) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = userService.getUser(username);

		if (!authService.checkIfValidOldPassword(user, passwordDto.getOldPassword())) {
			throw new BadRequestException("Invalid password!");
		}
		authService.changeUserPassword(user, passwordDto.getNewPassword());
		return ResponseEntity.ok(new ApiResponseDto(true, "Password updated successfully!"));
	}

}
