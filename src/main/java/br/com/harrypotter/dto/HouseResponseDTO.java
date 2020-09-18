package br.com.harrypotter.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class HouseResponseDTO {

	@JsonProperty(value = "_id")
	private String id;
	
	private String name;
	
	private String school;
	
}
