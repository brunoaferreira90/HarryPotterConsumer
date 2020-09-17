package br.com.harrypotter.config;

import java.util.UUID;

import lombok.Data;

@Data
public class CorrelationId {
	
	private final String id;
	
	public CorrelationId(String title){
		id =  title + "(" + UUID.randomUUID().toString() + ")";
	}
	
	public CorrelationId continueWith(String title) {
		return new CorrelationId(id + "-" + title);
	}

}
