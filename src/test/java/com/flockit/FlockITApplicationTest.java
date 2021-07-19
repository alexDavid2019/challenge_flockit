package com.flockit;

import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flockit.dto.ProvinciasDto;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Rollback
public class FlockITApplicationTest {

	
	@Before
	public void setUp() throws Exception {
		
	}
	
	@Test
	public void contextLoads() {
		assertNull(null);
	}

	@Test
	public void LombokTestApplicationTests() throws  JsonProcessingException {

        String anyString = "{\"cantidad\":24,\"inicio\":0,\"parametros\":{},\"provincias\":[{" +
        		"\"centroide\":{\"lat\":-26.8753965086829,\"lon\":-54.6516966230371},\"id\":\"54\",\"nombre\":\"Misiones\"}," +
		"{\"centroide\":{\"lat\":-33.7577257449137,\"lon\":-66.0281298195836},\"id\":\"74\",\"nombre\":\"San Luis\"}," +
		"{\"centroide\":{\"lat\":-30.8653679979618,\"lon\":-68.8894908486844},\"id\":\"70\",\"nombre\":\"San Juan\"}],\"total\":24}";

		ProvinciasDto mapResponse = new ProvinciasDto();
        ObjectMapper objectMapper = new ObjectMapper();

		mapResponse = objectMapper.readValue(anyString, new TypeReference<ProvinciasDto>() {});
		
		assertNull(mapResponse);

	        
	}

}
