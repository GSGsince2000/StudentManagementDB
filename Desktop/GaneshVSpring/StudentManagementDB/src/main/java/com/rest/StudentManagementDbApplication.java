package com.rest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.rest.controllers.StudentController;

@SpringBootApplication
public class StudentManagementDbApplication {

	public static void main(String[] args) {
		SpringApplication.run(StudentManagementDbApplication.class, args);
		
	}

}
