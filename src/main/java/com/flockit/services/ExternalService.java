package com.flockit.services;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flockit.dto.ProvinciasDto;
import com.flockit.dto.UbicacionesDto;

import reactor.core.publisher.Mono;

@Service
public class ExternalService {

	@Value("${api.datosgobar.url}")
	private String apiBaseUrl;
	@Value("${endpoint.datosgobar.provincias}")
	private String endpointProvincias;
	@Value("${endpoint.datosgobar.ubicacion}")
	private String endpointUbicacion;
	
	public Mono<ProvinciasDto> listProvincias() {
		
		WebClient webClient = WebClient.builder()
		        .baseUrl(apiBaseUrl)
		        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
		        .defaultHeader(HttpHeaders.USER_AGENT, "Spring 5 WebClient")
		        .build();
		/*
		Mono<ProvinciasDto> result = webClient.get()
	            .uri(endpointProvincias)
	            .accept(MediaType.APPLICATION_JSON)
	            .retrieve()
	            .bodyToMono(Map.class)
	            .flatMap(resp-> {
			            System.out.println("response :: "+resp);
			    		ObjectMapper mapper = new ObjectMapper();
			            ProvinciasDto content= mapper.convertValue(resp, ProvinciasDto.class);
			            System.out.println("content :: "+content);
			            return Mono.just(content);
	             }); 
		*/
		return webClient.get()
	            .uri(endpointProvincias)
	            .accept(MediaType.APPLICATION_JSON)
	            .retrieve()
	            .bodyToMono(ProvinciasDto.class); 
		
	}
	
	public Mono<ProvinciasDto> getProvinciaByName(String name) {
		
		WebClient webClient = WebClient.builder()
		        .baseUrl(apiBaseUrl)
		        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
		        .defaultHeader(HttpHeaders.USER_AGENT, "Spring 5 WebClient")
		        .build();
		
		Mono<ProvinciasDto> result = webClient.get()
	            .uri(endpointProvincias + "?nombre=" + name)
	            .accept(MediaType.APPLICATION_JSON)
	            .retrieve()
	            .bodyToMono(Map.class)
	            .flatMap(resp-> {
			            System.out.println("response :: "+resp);
			    		ObjectMapper mapper = new ObjectMapper();
			    		ProvinciasDto content= mapper.convertValue(resp, ProvinciasDto.class);
			            System.out.println("content :: "+content);
			            return Mono.just(content);
	             }); 
		
		return webClient.get()
	            .uri(endpointProvincias + "?nombre=" + name)
	            .accept(MediaType.APPLICATION_JSON)
	            .retrieve()
	            .bodyToMono(ProvinciasDto.class); 
		
	}
	
	public Mono<UbicacionesDto> getUbicacionByName(String name) {
		
		WebClient webClient = WebClient.builder()
		        .baseUrl(apiBaseUrl)
		        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
		        .defaultHeader(HttpHeaders.USER_AGENT, "Spring 5 WebClient")
		        .build();
		
		ProvinciasDto _prov = webClient.get()
	            .uri(endpointProvincias + "?nombre=" + name)
	            .accept(MediaType.APPLICATION_JSON)
	            .retrieve()
	            .bodyToMono(ProvinciasDto.class).block(); 
		
		return webClient.get()
	            .uri(endpointUbicacion + _prov.getUbicacion())
	            .accept(MediaType.APPLICATION_JSON)
	            .retrieve()
	            .bodyToMono(UbicacionesDto.class); 
		
	}
}
