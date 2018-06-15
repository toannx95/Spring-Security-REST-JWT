package com.spring.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.security.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	User findByUsername(String username);

	boolean existsByUsername(String username);

	boolean existsByEmail(String email);

}