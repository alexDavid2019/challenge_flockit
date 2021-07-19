package com.flockit.dto;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
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
public class ProvinciasDto {
    @JsonProperty(value = "cantidad", required = true)
	private Integer cantidad;
    @JsonProperty(value = "inicio", required = true)
	private Integer inicio;
    @JsonProperty(value = "total", required = true)
    private Integer total;	
    @JsonProperty(value = "provincias", required = false)
    private List<ProvinciaDto> provincias;
    @JsonProperty(value = "parametros")
    @JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    private Map parametros;

    public String getUbicacion() {
    	if (this.provincias != null && this.provincias.size() > 0) {
    		return this.provincias.get(0).getUbicacion();
    	}
    	return null;
    }
}
