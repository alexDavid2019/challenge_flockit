package com.flockit.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flockit.model.CustomUserDetails;
import com.flockit.model.UserModel;
import com.flockit.repository.UserRepository;

@Service("customUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

    	@Autowired
    	private final UserRepository repository;
    	
	    public CustomUserDetailsService(UserRepository repository) {
	        this.repository = repository;
	    }

	    @Override
	    @Transactional(readOnly = true)
	    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	    	/*
	    	final UserModel userModel = repository.findByUsername(username).orElseThrow(
	                () -> new UsernameNotFoundException(String.format("User = %s does not exists", username)));
	        
			return org.springframework.security.core.userdetails.User.withUsername(username)
	                   .password(userModel.getPassword())
	                   .roles(userModel.getRole().getValue())
	                   .accountExpired(false)
	 	  	           .accountLocked(false)
	 	  	           .credentialsExpired(false)
	 	  	           .disabled(false)
	 	  	           .build();
			*/
	    	
	        final Optional<UserModel> optionalUser = repository.findByUsername(username);
	        
	        return optionalUser.map(CustomUserDetails::new).orElseThrow(
	                () -> new UsernameNotFoundException(String.format("User = %s does not exists", username)));
	        
	    }	    
}
