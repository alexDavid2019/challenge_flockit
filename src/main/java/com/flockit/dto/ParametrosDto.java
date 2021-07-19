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
public class ParametrosDto {
    @JsonProperty(value = "lat", required = true)
	private Long lat;
    @JsonProperty(value = "lon", required = true)
	private Long lon;    
}
