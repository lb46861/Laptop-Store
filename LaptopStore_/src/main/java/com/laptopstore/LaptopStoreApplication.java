package com.laptopstore;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.laptopstore.domain.User;
import com.laptopstore.domain.security.Role;
import com.laptopstore.domain.security.UserRole;
import com.laptopstore.service.UserService;
import com.laptopstore.utility.SecurityUtility;

@SpringBootApplication
public class LaptopStoreApplication implements CommandLineRunner{

	@Autowired
	private UserService userService;
	
	public static void main(String[] args) {
		SpringApplication.run(LaptopStoreApplication.class, args);
	}
	
	@Override
	public void run(String... arg) throws Exception{
		User user1 = new User();
		user1.setFirstName("Test");
		user1.setLastName("Test");
		user1.setUsername("t");
		user1.setPassword(SecurityUtility.passwordEncoder().encode("t"));
		user1.setEmail("TestMail@gmail.com");
		Set<UserRole> userRoles = new HashSet<>();
		Role role1 = new Role();
		role1.setRoleId(1);
		role1.setName("ROLE_USER");
		userRoles.add(new UserRole(user1, role1));
		
		userService.createUser(user1, userRoles);
	}

}
