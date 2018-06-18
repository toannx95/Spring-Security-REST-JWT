package com.spring.security;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.spring.security.entity.Role;
import com.spring.security.entity.User;
import com.spring.security.enumeration.RoleEnum;
import com.spring.security.repository.RoleRepository;
import com.spring.security.repository.UserRepository;

@Component
public class DataSeedingListener implements ApplicationListener<ContextRefreshedEvent> {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		// init roles
		String adminRoleName = RoleEnum.ADMIN.getRole();
		String memberRoleName = RoleEnum.MEMBER.getRole();

		if (!roleRepository.existsByName(adminRoleName)) {
			roleRepository.save(new Role(adminRoleName));
		}

		if (!roleRepository.existsByName(memberRoleName)) {
			roleRepository.save(new Role(memberRoleName));
		}

		Role adminRole = roleRepository.findByName(adminRoleName);
		Role memberRole = roleRepository.findByName(memberRoleName);

		// init users
		if (!Objects.isNull(adminRole) && !Objects.isNull(memberRole)) {
			if (!userRepository.existsByUsername("admin")) {
				User user = new User("admin", passwordEncoder.encode("admin"), "admin@gmail.com");
				user.setRoles(new HashSet<>(Arrays.asList(adminRole, memberRole)));
				userRepository.save(user);
			}

			if (!userRepository.existsByUsername("member")) {
				User user = new User("member", passwordEncoder.encode("member"), "member@gmail.com");
				user.setRoles(new HashSet<>(Arrays.asList(memberRole)));
				userRepository.save(user);
			}
		}
	}

}
