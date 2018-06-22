package com.spring.security.service;

import com.spring.security.dto.SignUpDto;
import com.spring.security.entity.User;

public interface AuthService {

	User registerUser(SignUpDto signUpRequest);

	boolean checkIfValidOldPassword(User user, String oldPassword);

	void changeUserPassword(User user, String password);
}
