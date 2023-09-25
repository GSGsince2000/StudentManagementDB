package com.rest.serviceimpl;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.rest.dto.StudentDtoReq;
import com.rest.dto.StudentDtoRes;
import com.rest.helpers.PostResponse;
import com.rest.models.Student;
import com.rest.repositories.StudentRepository;
import com.rest.services.StudentExcelService;
import com.rest.services.StudentService;


@Service
public class StudentServiceImpl implements StudentService {

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private StudentEmailService emailService;

	@Autowired
	private StudentExcelService excelService;
	
	private Student stud=new Student();

	private UUID uuid;

	@Override
	public List<StudentDtoRes> getAllStuds() {
		List<Student> stud= studentRepository.findAll();
		List<StudentDtoRes> arDto= new ArrayList<>();
		for(Student stu : stud) {
			StudentDtoRes dto = new StudentDtoRes();
			BeanUtils.copyProperties(stu, dto);
			//			BeanUtils.copyProperties(stu, dto, "phoneNo", "location");
			arDto.add(dto);
		}		
		return arDto;
	}
	// Pagination 
	@Override
	public PostResponse getAllStuds(Integer pageNo, Integer pageSize) {
		Pageable p = PageRequest.of(pageNo, pageSize);
		Page<Student> pageStud= studentRepository.findAll(p);
		List<Student> allStud= pageStud.getContent();
		List<StudentDtoRes> arDto= new ArrayList<>();
		for(Student stu : allStud) {
			StudentDtoRes dto = new StudentDtoRes();
			BeanUtils.copyProperties(stu, dto);
			arDto.add(dto);
		}
		PostResponse postRes = new PostResponse();

		postRes.setContent(arDto);
		postRes.setPageNumber(pageStud.getNumber());
		postRes.setPageSize(pageStud.getSize());
		postRes.setTotalPages(pageStud.getTotalPages());
		postRes.setTotalElements(pageStud.getNumberOfElements());
		postRes.setLastPage(pageStud.isLast());
		return postRes;
	}

	@Override
	public StudentDtoRes getStudByID(Long rollNo) {
		Student st=studentRepository.findById(rollNo).orElseThrow(
				()-> {throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Given Roll No Does not Exist.");});
		StudentDtoRes dto= new StudentDtoRes();
		BeanUtils.copyProperties(st, dto);
		return dto;
	}

	@Override
	public void saveStudent(StudentDtoReq studentDto) {
		validate(studentDto);
		studentDto.setUuid(uuid.randomUUID().toString());
		BeanUtils.copyProperties(studentDto, stud);
		studentRepository.save(stud);
		//		try {
		//			File myFile=new File("C:\\Users\\ts\\Desktop\\ProjTasks\\JSONBackup\\stud.text");
		//		ObjectMapper objm= new ObjectMapper();
		//		objm.writeValue(myFile, stud);
		//		System.out.println(stud);
		//		objm.clearProblemHandlers();
		//		}
		//		catch (Exception e) {
		//			e.printStackTrace();
		//		}
		emailService.sendMail(stud);
	}

	@Override
	public void updateStud(Long rollNo, StudentDtoReq studentDto) {
		this.getStudByID(rollNo);
		BeanUtils.copyProperties(studentDto, stud);
		stud.setRollNo(rollNo);
		studentRepository.save(stud);

	}

	@Override
	public void deleteByRollNo(Long rollNo) {
		studentRepository.deleteById(rollNo);
	}

	private void validate(StudentDtoReq dto) {
		try {
			if((dto.getFirstName()=="") || (dto.getFirstName()==null)){
				throw new Exception("First Name Can not be NULL");
			}
			if((dto.getLastName()=="") || (dto.getLastName()==null) ) {
				throw new Exception("Last Name can not be NULL");
			}
			if((dto.getLocation()=="")|| (dto.getLocation()==null)) {
				throw new Exception("Location Can not be NULL");
			}
			if((dto.getPhoneNo()=="") || (dto.getPhoneNo()==null)) {
				throw new Exception("Phone Can not be NULL");
			}
			if(dto.getPhoneNo().length()!=10) {
				throw new Exception("Invalid Phone Number Details");
			}
		}
		catch(Exception e) {

		}

	}

	@Override
	public void sendMailToAll() {
		emailService.sendMailToAll();	
	}

	@Override
	public ByteArrayInputStream downStudInfoExcel() {
		List<Student> stLis= studentRepository.findAll();
		List<StudentDtoReq> dtoLis= new ArrayList<>();
		for(Student stu : stLis) {
			StudentDtoReq dto = new StudentDtoReq();
			BeanUtils.copyProperties(stu, dto);
			dtoLis.add(dto);
		}
		ByteArrayInputStream in = excelService.downStudInfoExcel(dtoLis);	
		return in;
	}
	@Override
	public StudentDtoRes getStudByName(String name) {
		Student stud=studentRepository.getStudentByFirstName(name);
		StudentDtoRes dto= new StudentDtoRes();
		BeanUtils.copyProperties(stud, dto);
		return dto;
	}

	@Override
	public StudentDtoRes getStudentByLastName(String lastName) {
		Student stud=studentRepository.getStudentByLastName(lastName);
		StudentDtoRes dto= new StudentDtoRes();
		BeanUtils.copyProperties(stud, dto);
		return dto;
	}
	@Override
	public List<StudentDtoRes> getStudentByFirstNameorLastName(String name) {
		List<Student> stuLis= studentRepository.getStudentByFirstNameOrLastName(name, name);
		List<StudentDtoRes> dtoLis = new ArrayList<>();
		for(Student stu : stuLis) {
			StudentDtoRes dto = new StudentDtoRes();
			BeanUtils.copyProperties(stu, dto);
			dtoLis.add(dto);
		}		
		return dtoLis;
	}
}
