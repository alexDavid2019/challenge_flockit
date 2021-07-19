package com.flockit.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProvinciaDto {
    @JsonProperty(value = "centroide", required = false)
	private CentroideDto centroide;
    @JsonProperty(value = "id", required = true)
    private Long id;
    @JsonProperty(value = "nombre", required = true)
    private String nombre;
    
    public String getUbicacion() {
    	return this.centroide != null? this.centroide.getUbicacion(): null;
    }
}
