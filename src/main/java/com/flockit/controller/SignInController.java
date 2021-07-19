package com.flockit.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flockit.dto.GenericResponseDto;
import com.flockit.dto.LoginRequestDto;
import com.flockit.enums.Status;
import com.flockit.model.UserModel;
import com.flockit.repository.UserRepository;
import com.flockit.services.UserService;
import com.flockit.utils.SecurityConstants;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@Validated
@RequestMapping(value = "/api/signin")
@Api(tags = "access")
public class SignInController {

	@Autowired
	private UserRepository userRepository;
	@Autowired
    private PasswordEncoder passwordEncoder;
	@Autowired
	private UserService userService;

    @CrossOrigin()
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequestDto userRequest) {
        
    	try
    	{
	        List<UserModel> users = userRepository.findAll();
	        Status _result = null;
	        String _token = null;
	        
	        for (UserModel other : users) 
	        {
	            Boolean isPasswordMatch  = passwordEncoder.matches(userRequest.getPassword(), other.getPassword());;
	            Boolean isUserMatch = other.getUsername().equals(userRequest.getUsername());
	            if (isPasswordMatch && isUserMatch) 
	            {
	                _token = userService.signin(userRequest.getUsername(), userRequest.getPassword());
	            	other.setLoggedIn(true);
	                userRepository.save(other);
	                _result = Status.SUCCESS;
	            }
	        }
	        
	        if (_result != null) {
	        	
	        	GenericResponseDto<Map<String,String>> _ret = new GenericResponseDto<Map<String,String>>(null,
	        			_result,null);
	        	 Map<String,String> _map = new HashMap<String,String>();
				 _map.put(SecurityConstants.TOKEN_HEADER, SecurityConstants.TOKEN_PREFIX + _token);
		     	_ret.setData(_map);
	        	
	        	return ResponseEntity.status(HttpStatus.OK).body(_ret);
	        }

        	GenericResponseDto<String> _ret = new GenericResponseDto<String>(Strings.EMPTY,
        												Status.NOT_FOUND,
        												Arrays.asList("username not found"));
    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(_ret);

    	} catch (Exception ex) {
        	GenericResponseDto<String> _ret = new GenericResponseDto<String>(Strings.EMPTY,
        												Status.FAILURE,
        												Arrays.asList(ex.getMessage()));
    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(_ret);
    	}
    }  
}
