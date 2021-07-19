package com.flockit.controller;

import java.security.Principal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flockit.dto.GenericResponseDto;
import com.flockit.enums.Status;
import com.flockit.enums.User_Role;
import com.flockit.model.UserModel;
import com.flockit.repository.UserRepository;
import com.flockit.services.UserService;
import com.flockit.utils.SecurityConstants;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/users")
@Api(tags = "users")
@Slf4j
@Validated
public class UserController {

	@Autowired
	private UserRepository userRepository;	
	@Autowired
	private UserService userService;
	
	@CrossOrigin()
	@GetMapping("/logout")
	@PreAuthorize("isAuthenticated()")  
	public ResponseEntity<?> logUserOut(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
		if (principal == null) {
        	GenericResponseDto<String> _ret = new GenericResponseDto<String>(Strings.EMPTY,
        							Status.FAILURE,
        							Arrays.asList("role not authorized"));
    		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(_ret);
	    }
		
		try
    	{
            String auth = request.getUserPrincipal() == null ? null : request.getUserPrincipal().getName();
            
        	List<UserModel> users = userRepository.findAll();
	        
	        Status _result = null;
	        for (UserModel other : users) 
	        {
	            if (other.getUsername().endsWith(auth))
	            {
	            	other.setLoggedIn(false);
	                userRepository.save(other);
	                _result = Status.SUCCESS;
	            }
	        }
	        
	        if (_result != null) 
	        {
				GenericResponseDto<Map<String,String>> _ret = new GenericResponseDto<Map<String,String>>(new HashMap<String,String>(),
						_result,null);
				 Map<String,String> _map = new HashMap<String,String>();
				 _map.put("logout", "ok");
		     	_ret.setData( _map );

	    		return ResponseEntity.status(HttpStatus.OK).body(_ret);
	        }
	        else {
	        	GenericResponseDto<String> _ret = new GenericResponseDto<String>(Strings.EMPTY,
	        								Status.NOT_FOUND,
	        								Arrays.asList("username not found"));
	    		return ResponseEntity.status(HttpStatus.OK).body(_ret);
	        }
    	} catch (Exception ex) {
        	GenericResponseDto<String> _ret = new GenericResponseDto<String>(Strings.EMPTY,
        										Status.FAILURE,
        										Arrays.asList(ex.getMessage()));
    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(_ret);
    	}
    }	
	
	@CrossOrigin()
    @GetMapping(value = "/me")
	@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
	public ResponseEntity<?> whoami(HttpServletRequest request) {
	    
		if (!request.isUserInRole(User_Role.ROLE_ADMIN.getValue()) && !request.isUserInRole(User_Role.ROLE_USER.getValue())) {
        	GenericResponseDto<String> _ret = new GenericResponseDto<String>(Strings.EMPTY,
        											Status.FAILURE,
        											Arrays.asList("role not authorized"));
    		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(_ret);
	    }
	    
		try {
			GenericResponseDto<Map<String,String>> _ret = new GenericResponseDto<Map<String,String>>(new HashMap<String,String>(),Status.SUCCESS,null);

			 Map<String,String> _map = new HashMap<String,String>();
			 _map.put("whoami", userService.whoami(request));
	     	_ret.setData( _map );
	     	
	 		return ResponseEntity.status(HttpStatus.OK).body(_ret);
	 		
    	} catch (Exception ex) {
        	GenericResponseDto<String> _ret = new GenericResponseDto<String>(Strings.EMPTY,
        										Status.FAILURE,
        										Arrays.asList(ex.getMessage()));
    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(_ret);
    	}
	 }

	 @GetMapping("/refresh")
	 @PreAuthorize("hasRole('ADMIN')")
	 public ResponseEntity<?> refresh(HttpServletRequest request) {
		 
		 if (!request.isUserInRole(User_Role.ROLE_ADMIN.getValue())) {
	        	GenericResponseDto<String> _ret = new GenericResponseDto<String>(Strings.EMPTY,
	        								Status.FAILURE,
	        								Arrays.asList("role not authorized"));
	    		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(_ret);
		 }
		 
		 try {
			 
			GenericResponseDto<Map<String,String>> _ret = new GenericResponseDto<Map<String,String>>(new HashMap<String,String>(),Status.SUCCESS,null);

	     	String _token = userService.refresh(request);
	     	Map<String,String> _map = new HashMap<String,String>();
			 _map.put(SecurityConstants.TOKEN_HEADER, SecurityConstants.TOKEN_PREFIX + _token);
	     	_ret.setData(_map);

	 		return ResponseEntity.status(HttpStatus.OK).body(_ret);
	 		
    	} catch (Exception ex) {
        	GenericResponseDto<String> _ret = new GenericResponseDto<String>(Strings.EMPTY,
        											Status.FAILURE,
        											Arrays.asList(ex.getMessage()));
    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(_ret);
    	}
	 }	  
}
