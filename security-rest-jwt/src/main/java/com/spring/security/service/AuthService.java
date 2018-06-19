package com.spring.security.service;

import com.spring.security.dto.LoginRequest;
import com.spring.security.dto.SignUpRequest;
import com.spring.security.entity.User;

public interface AuthService {

	String authenticateUser(LoginRequest loginRequest);

	User registerUser(SignUpRequest signUpRequest);
}
