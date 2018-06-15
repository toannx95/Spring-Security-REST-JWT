package com.spring.security.enumeration;

public enum RoleEnum {

	ADMIN("ROLE_ADMIN"), 
	MEMBER("ROLE_MEMBER");

	private String role;

	private RoleEnum(String role) {
		this.role = role;
	}

	public String getRole() {
		return role;
	}

}
