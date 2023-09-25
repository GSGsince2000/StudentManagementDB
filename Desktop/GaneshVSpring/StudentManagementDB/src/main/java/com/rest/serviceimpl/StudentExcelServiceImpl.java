package com.rest.serviceimpl;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.rest.dto.StudentDtoReq;
import com.rest.models.Student;
import com.rest.repositories.StudentRepository;


@Service
public class StudentExcelServiceImpl  implements com.rest.services.StudentExcelService{
	@Autowired
	StudentRepository studentRepository;

	public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
	static String[] HEADERs = { "Roll No.", "First Name", "Last Name", "Location","Phone No","email"};
	static String SHEET = "STUDENTS DATA";

	public ByteArrayInputStream downStudInfoExcel(List<StudentDtoReq> dtoLis) {

		try{
			Workbook workbook = new XSSFWorkbook(); 

			ByteArrayOutputStream out = new ByteArrayOutputStream();
			Sheet sheet = workbook.createSheet(SHEET);

			// Header
			Row headerRow = sheet.createRow(0);

			for (int col = 0; col < HEADERs.length; col++) {
				Cell cell = headerRow.createCell(col);
				cell.setCellValue(HEADERs[col]);
			}

			int rowIdx = 1;
			for (StudentDtoReq dto : dtoLis) {
				Row row = sheet.createRow(rowIdx++);
				int c=0;

				row.createCell(c++).setCellValue(dto.getRollNo());
				row.createCell(c++).setCellValue(dto.getFirstName());
				row.createCell(c++).setCellValue(dto.getLastName());
				row.createCell(c++).setCellValue(dto.getLocation());
				row.createCell(c++).setCellValue(dto.getPhoneNo());
				row.createCell(c++).setCellValue(dto.getEmail());
			}

			workbook.write(out);
			return new ByteArrayInputStream(out.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
		}
	}

	@Override
	public void save(MultipartFile file) {
		try {
			List<Student> stud= excelToStudents(file.getInputStream());
			studentRepository.saveAll(stud);
		}catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("fail to store the Data"+e.getMessage());
		}

	}

	private List<Student> excelToStudents(InputStream iS) {
		try {
			Workbook workbook = new XSSFWorkbook(iS); //workbook of uploaded file
			Sheet sheet = workbook.getSheet(SHEET); //sheet of the uploaded file (Sheet name is Blank so it can take any sheet to read
			Iterator<Row> rows =sheet.iterator(); // to iterate over the data inside sheet

			List<Student> stuLi=new ArrayList<>();

			int rowIdx=0;
			while(rows.hasNext()) {
				Row curRow= rows.next();

				//To skip the Header
				if(rowIdx==0) {
					rowIdx++;
					continue;
				}
				Iterator<Cell> cellsInRow= curRow.iterator();// to iterate over the cells in row

				Student student =new Student(); // to set the value

				int cellIdx=0;
				while(cellsInRow.hasNext()) {
					Cell curCell= cellsInRow.next();
					switch (cellIdx) {
					case 0:
						student.setRollNo((long) curCell.getNumericCellValue());
						break;

					case 1:
						student.setFirstName(curCell.getStringCellValue());
						break;

					case 2:
						student.setLastName(curCell.getStringCellValue());
						break;

					case 3:
						student.setLocation(curCell.getStringCellValue());
						break;

					case 4:
						student.setPhoneNo(curCell.getStringCellValue());
						break;

					case 5:
						student.setEmail(curCell.getStringCellValue());
						break;
					default:
						break;
					}
					cellIdx++;
				}
				stuLi.add(student);
			}
			workbook.close();
			return stuLi;
		}
		catch (Exception e) {
			throw new RuntimeException("Fail to Read the Excel File.");
		}
	}
}
