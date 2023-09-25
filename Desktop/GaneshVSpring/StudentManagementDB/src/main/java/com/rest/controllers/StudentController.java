package com.rest.controllers;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.rest.dto.StudentDtoReq;
import com.rest.dto.StudentDtoRes;
import com.rest.helpers.Cons;
import com.rest.helpers.PostResponse;
import com.rest.messages.ResponseMessage;
import com.rest.services.StudentExcelService;
import com.rest.services.StudentLoggerService;
import com.rest.services.StudentService;


@RestController
@RequestMapping("/students")
public class StudentController{
	
	//	@Autowired
	//	StudentService stuService;

	// for extra security
	private final StudentService stuService;

	public StudentController(StudentService stuService1) {
		this.stuService = stuService1;
	}

	@Autowired
	StudentExcelService excelService;
	
	@Autowired
	StudentLoggerService loggerService;

	@GetMapping("/all-studs")
	public List<StudentDtoRes> getAllStudLi() {
		loggerService.info(loggerService.Fetched);
		return stuService.getAllStuds();
	}

	@GetMapping("/all-studs-p")//Pagination
	public ResponseEntity<PostResponse> getAllStudLi(
			@RequestParam(value = "pageNo", defaultValue = "0", required = false) Integer pageNo,
			@RequestParam(value = "pageSize", defaultValue = "3", required = false) Integer pageSize
			) {
		PostResponse post = stuService.getAllStuds(pageNo, pageSize);
		loggerService.info(loggerService.Fetched);
		return new ResponseEntity<PostResponse>(post, HttpStatus.OK);
	}

	@GetMapping("/{rollNo}")
	public ResponseEntity<StudentDtoRes> getStudByRollNo(@PathVariable Long rollNo) {
		loggerService.info(loggerService.FetchedById);
		StudentDtoRes stu=stuService.getStudByID(rollNo);
		Cons con = Cons.STUDENT_FOUND;
		System.out.println(con.name());
		return new ResponseEntity<>(stu, HttpStatus.FOUND);
	}
	
	@GetMapping("/getStudentByName")
	public ResponseEntity<?> getStudByFirstName(@RequestParam String firstName) {
		StudentDtoRes stud= stuService.getStudByName(firstName);
		return new ResponseEntity<>(stud,HttpStatus.FOUND);		
	}
	
	@GetMapping("/getStudentByLastName")
	public ResponseEntity<?> getStudntByLastName(@RequestParam String lastName){
		StudentDtoRes stud = stuService.getStudentByLastName(lastName);
		return new ResponseEntity<>(stud,HttpStatus.FOUND);
	}
	
	@GetMapping("/getStudentByFirstNameorLastName")
	public ResponseEntity<List<StudentDtoRes>> getStudentByFirstNameorLastName(@RequestParam String name) {
		List<StudentDtoRes> stud = stuService.getStudentByFirstNameorLastName(name);
		return new ResponseEntity<>(stud,HttpStatus.FOUND);
	}
	
	@PostMapping("/save")
	public void saveStudent(@RequestBody StudentDtoReq studentDto) {
		loggerService.info(loggerService.Saved);
		stuService.saveStudent(studentDto);
	}

	@PutMapping("/{rollNo}")
	public void updateStudent(@PathVariable Long rollNo, @RequestBody StudentDtoReq studentDto) {
		loggerService.info(loggerService.Updated);
		stuService.updateStud(rollNo,studentDto);
	}

	@DeleteMapping("/{rollNo}")
	public void deleteStudByrollNo(@PathVariable Long rollNo) {
		loggerService.info(loggerService.Deleted);
		stuService.deleteByRollNo(rollNo);
	}

	@PostMapping("/send-email")
	public void sentMailToAll() {
		stuService.sendMailToAll();
	}

	@GetMapping("/download")
	public ResponseEntity<Resource> downStudsDataInExcel(){
		String fileName="StudentsData.xlsx";
		InputStreamResource file = new InputStreamResource(stuService.downStudInfoExcel());

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
				.contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
				.body(file);
	}

	@PostMapping("/upload")
	public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file){
		String type = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
		String message="";

		if(type.equals(file.getContentType())) {
			try {
				excelService.save(file);

				message="Uploaded File Successfully: " + file.getOriginalFilename();
				return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
			}catch (Exception e) {
				message="Could Not Upload The File: " + file.getOriginalFilename();
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));	
			}
		}
		message="Please Upload The Excel File";
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
	}
}