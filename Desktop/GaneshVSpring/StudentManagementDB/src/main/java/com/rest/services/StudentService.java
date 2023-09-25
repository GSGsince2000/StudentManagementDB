package com.rest.services;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import com.rest.dto.StudentDtoReq;
import com.rest.dto.StudentDtoRes;
import com.rest.helpers.PostResponse;
import com.rest.models.Student;

public interface StudentService {
	
	List<StudentDtoRes> getAllStuds();
	
	//Pagination
	PostResponse getAllStuds(Integer pageNo, Integer pageSize); 

	StudentDtoRes getStudByID(Long rollNo);

	void saveStudent(StudentDtoReq studentDto);

	void updateStud(Long rollNo, StudentDtoReq studentDto);

	void deleteByRollNo(Long rollNo);
	
	
	void sendMailToAll();
	
	//Excel
	ByteArrayInputStream downStudInfoExcel();
	
	// JPA Custom Methods
	StudentDtoRes getStudByName(String firstName);

	StudentDtoRes getStudentByLastName(String lastName);

	List<StudentDtoRes> getStudentByFirstNameorLastName(String name);

	
	

	

}
