package com.flockit.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.flockit.enums.Status;

public class GenericResponseDto<T> {

    @JsonProperty("data")
    private T data;
    @JsonProperty("errors")
    private List<String> errors;
    @JsonProperty("status")
    private Status status;

    public GenericResponseDto() {}
    
    public GenericResponseDto(T data, Status status, List<String> errors) {
    	this.data = data;
    	this.status = status;
    	this.errors = errors;
    }
    
    public T getData() {
        return data;
    }

    public void setData(T val) {
        this.data = val;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status val) {
        this.status = val;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> val) {
        this.errors = val;
    }

}
