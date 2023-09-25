package com.rest.serviceimpl;

import org.springframework.stereotype.Service;

import com.rest.services.StudentLoggerService;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class StudentLoggerServiceImpl implements StudentLoggerService{

	public void info(String message) {
		log.info(message);
	}







}
