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
public class MunicipioDto {
    @JsonProperty(value = "id", required = true)
    private Long id;
    @JsonProperty(value = "nombre", required = true)
    private String nombre;
    
}
