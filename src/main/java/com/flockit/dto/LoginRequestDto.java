package com.flockit.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginRequestDto {

	@NotBlank
    @NotEmpty
    @JsonProperty("username")
    private String username;

	@NotBlank
    @NotEmpty
    @JsonProperty("password")
    private String password;

	 public String getUsername() {
        return username;
	 }

	 public void setUsername(String val) {
        this.username = val;
	 }

	 public String getPassword() {
        return password;
	 }

	 public void setPassword(String val) {
        this.password = val;
	 }

}
