package com.rest.models;
import lombok.Data;

@Data
public class EmailDetails {
	private String recipient;
	
	private String message;
	
	private String subject;
	
	private String attachment; 
}
