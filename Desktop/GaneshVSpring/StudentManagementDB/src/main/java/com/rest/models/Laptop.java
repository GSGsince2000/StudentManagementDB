package com.rest.models;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Laptop {
	
	@Id
	private int modelNo;
	
	private String brandName;
}
