package com.flockit.services;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.flockit.model.UserModel;
import com.flockit.repository.UserRepository;
import com.flockit.security.JwtTokenProvider;

@Service
public class UserService {

	  @Autowired
	  private UserRepository userRepository;

	  @Autowired
	  private PasswordEncoder passwordEncoder;

	  @Autowired
	  private JwtTokenProvider jwtTokenProvider;

	  @Autowired
	  private AuthenticationManager authenticationManager;

	  public String signin(String username, String password) throws Exception {
	    try {
	      authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
	      return jwtTokenProvider.createToken(username, userRepository.findByUsername(username).get().getRole());
	    } catch (AuthenticationException e) {
	      throw new Exception("Invalid username/password supplied");
	    }
	  }

	  public String register(UserModel user) throws Exception {
	    if (!userRepository.existsByUsername(user.getUsername())) 
	    {
	       	user.setPassword(passwordEncoder.encode(user.getPassword()));
	    	userRepository.save(user);
	    	return jwtTokenProvider.createToken(user.getUsername(), user.getRole());
	    } 
	    else {
	      throw new Exception("Username is already in use");
	    }
	    
	  }
	  
	  public String whoami(HttpServletRequest req) throws Exception 
	  {
		  	Optional<UserModel> user = userRepository.findByUsername(jwtTokenProvider.getUsername(jwtTokenProvider.resolveToken(req)));
		    if (!user.isPresent()) {
			      throw new Exception("The user doesn't exist");
			}
		    return user.get().getUsername();
	  }
	  
	  public String refresh(HttpServletRequest req) {
		  return jwtTokenProvider.refreshToken(req);
	  }

}
