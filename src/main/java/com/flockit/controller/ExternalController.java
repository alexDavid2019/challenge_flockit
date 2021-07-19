package com.flockit.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.flockit.dto.ProvinciaDto;
import com.flockit.dto.ProvinciasDto;
import com.flockit.dto.UbicacionesDto;
import com.flockit.services.ExternalService;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@Validated
@RequestMapping(value = "/api/georef")
@Api(tags = "external")
public class ExternalController {
	
	@Autowired
	private ExternalService externalService;
	
	@CrossOrigin()
    @GetMapping(value = "/provincias")
	public Mono<ResponseEntity<ProvinciasDto>> getProvincias(HttpServletRequest request) {
		return externalService.listProvincias()
				.map(list -> ResponseEntity.ok(list))
				.defaultIfEmpty(ResponseEntity.badRequest().build());
	}

	@CrossOrigin()
    @GetMapping(value = "/provincias/{name}")
	public Mono<ResponseEntity<ProvinciasDto>> getByName(@PathVariable(value = "name")
	  		@NotNull
	  		@NotBlank
	  		final String name){
		return externalService.getProvinciaByName(name)
				.map(resp -> ResponseEntity.ok(resp))
				.defaultIfEmpty(ResponseEntity.badRequest().build());  
	}
	
	@CrossOrigin()
    @GetMapping(value = "/ubicacion/{name}")
	public Mono<ResponseEntity<UbicacionesDto>> getUbicacionByName(@PathVariable(value = "name")
	  		@NotNull
	  		@NotBlank
	  		final String name){
		return externalService.getUbicacionByName(name)
				.map(resp -> ResponseEntity.ok(resp))
				.defaultIfEmpty(ResponseEntity.badRequest().build());  
	}
}
