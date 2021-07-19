package com.flockit.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.flockit.model.UserModel;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class LoadDatabase {

	 @Value("${data.users:admin,user1,user2}")
	 private String[] users;
	   
	 @Autowired
	 private PasswordEncoder passwordEncoder;

	 @Value("${jwt.clientId:myclientIdDefault}")
	 private String clientId;

	 @Value("${jwt.client-secret:mysecretDefault}")
	 private String clientSecret;
	 
	 @Bean
	 CommandLineRunner initUsers(UserRepository repo) {
	        return args -> {
                String pwd = passwordEncoder.encode("12345678");
	            for (int i = 0; i < users.length; i++) {
	                String username = users[i];
	                com.flockit.enums.User_Role role = i > 1 ? com.flockit.enums.User_Role.ROLE_USER : i == 0 ? com.flockit.enums.User_Role.ROLE_ADMIN : com.flockit.enums.User_Role.ROLE_USER;
	                repo.save(new UserModel(username, pwd, role));
	            }
	        };
	    }
}
