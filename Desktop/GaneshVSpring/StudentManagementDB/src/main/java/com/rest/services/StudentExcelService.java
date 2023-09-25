package com.rest.services;

import java.io.ByteArrayInputStream;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.rest.dto.StudentDtoReq;

public interface StudentExcelService {

	public ByteArrayInputStream downStudInfoExcel(List<StudentDtoReq> dtoLis);

	public void save(MultipartFile file);
}
