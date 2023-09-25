package com.rest.repositories;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.rest.models.Student;

public interface StudentRepository extends JpaRepository<Student, Long>{

	@Query("select s.email from Student s") //MODEL CLASS NAME REQ
	public String[] getAllStudsMail();
	
	
	public Student getStudentByFirstName(String name); 
	
	public Student getStudentByLastName(String lastName);
	
	public List<Student> getStudentByFirstNameOrLastName(String firstName, String lastName);

}
