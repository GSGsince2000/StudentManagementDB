package com.rest.helpers;

import java.util.List;

import com.rest.dto.StudentDtoRes;

import lombok.Data;

@Data
public class PostResponse {
	
	private List<StudentDtoRes> content;
	private int pageNumber;
	private int pageSize;
	private int totalElements;
	private int totalPages;
	private boolean lastPage;
}
