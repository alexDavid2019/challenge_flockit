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
public class UbicacionesDto {
    @JsonProperty(value = "parametros")
	private ParametrosDto parametros;
    @JsonProperty(value = "ubicacion")
	private UbicacionDto ubicacion;
}
