package com.flockit;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.flockit.dto.GenericResponseDto;
import com.flockit.enums.Status;
import com.flockit.security.JwtTokenProvider;
import com.flockit.utils.SecurityConstants;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")   
public class TokenAuthenticationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
	private JwtTokenProvider jwtTokenProvider;

    @Test
    public void controllerUsersShouldNotAllowAccessToUnauthenticatedUsersEndpointMeTest() throws Exception {
    	mvc.perform(MockMvcRequestBuilders.get("/api/users/me"))
        		.andExpect(status().isForbidden());
    }

    @Test
    public void controllerUsersShouldNotAllowAccessToUnauthenticatedUsersEndpointLogoutTest() throws Exception {
    	mvc.perform(MockMvcRequestBuilders.get("/api/users/logout"))
        		.andExpect(status().isForbidden());
    }

    @Test
    public void controllerUsersShouldAllowAccessToAuthenticatedUsersEndpointLogoutTest() throws Exception {
    	String token = jwtTokenProvider.createToken("admin", com.flockit.enums.User_Role.ROLE_USER );
        Assert.assertNotNull(token);
    	MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/api/users/logout")
        		.contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
        		.header(SecurityConstants.TOKEN_HEADER,SecurityConstants.TOKEN_PREFIX + token))
    			.andExpect(status().isOk()).andReturn();
        String response = result.getResponse().getContentAsString();
        GenericResponseDto<Map<String,String>> mapResponse = new GenericResponseDto<Map<String,String>>();
        ObjectMapper objectMapper = new ObjectMapper();
        mapResponse = objectMapper.readValue(response, new TypeReference<GenericResponseDto<Map<String,String>>>() {});

        Assert.assertNotNull(mapResponse.getData());
        Assert.assertEquals(mapResponse.getStatus(), Status.SUCCESS);
    }
    
    @Test
    public void controllerUsersShouldGenerateAuthTokenToAccessEndpointMeTest() throws Exception {
        String token = jwtTokenProvider.createToken("admin", com.flockit.enums.User_Role.ROLE_ADMIN );
        Assert.assertNotNull(token);

    	MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/api/users/me")
        		.contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
        		.header(SecurityConstants.TOKEN_HEADER,SecurityConstants.TOKEN_PREFIX + token))
        		.andExpect(status().isOk()).andReturn();
        String response = result.getResponse().getContentAsString();
        GenericResponseDto<Map<String,String>> mapResponse = new GenericResponseDto<Map<String,String>>();
        ObjectMapper objectMapper = new ObjectMapper();
        mapResponse = objectMapper.readValue(response, new TypeReference<GenericResponseDto<Map<String,String>>>() {});

        Assert.assertNotNull(mapResponse.getData());
        Assert.assertEquals(mapResponse.getStatus(), Status.SUCCESS);
    }
    
    @Test
    public void controllerSignInValidUserCanGetTokenAndAuthenticationEndpointLoginTest() throws Exception {
    	
        String username = "admin";
        String password = "12345678";

        String body = "{\"username\":\"" + username + "\", \"password\":\"" + password + "\"}";

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/api/signin/login")
        		.contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(body))
                .andExpect(status().isOk()).andReturn();

        String response = result.getResponse().getContentAsString();
        //output: Body = {"data":{"Authorization":"Bearer ey.....ZA"},"errors":null,"status":"SUCCESS"}
        
        GenericResponseDto<Map<String,String>> mapResponse = new GenericResponseDto<Map<String,String>>();
        ObjectMapper objectMapper = new ObjectMapper();
        mapResponse = objectMapper.readValue(response, new TypeReference<GenericResponseDto<Map<String,String>>>() {});

        String token = mapResponse.getData().get(SecurityConstants.TOKEN_HEADER).replace(SecurityConstants.TOKEN_PREFIX, "");
        
        mvc.perform(MockMvcRequestBuilders.get("/api/users/me")
        		.contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
        		.header(SecurityConstants.TOKEN_HEADER,SecurityConstants.TOKEN_PREFIX + token))
        		.andExpect(status().isOk());
    }
    
    @Test
    public void controllerSignInNoValidUserCannotGetTokenEndpointLoginTest() throws Exception {
        String username = "nonexistentuser";
        String password = "password";

        String body = "{\"username\":\"" + username + "\", \"password\":\"" + password + "\"}";

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/api/signin/login")
        		.contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
        		.content(body))
                .andExpect(status().isBadRequest()).andReturn();
    }    
}
