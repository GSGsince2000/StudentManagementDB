package com.rest.serviceimpl;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.rest.models.EmailDetails;
import com.rest.models.Student;
import com.rest.repositories.StudentRepository;

import jakarta.mail.internet.MimeMessage;
@Component
public class StudentEmailService {

	@Autowired
	StudentRepository studentRepository;

	@Autowired
	JavaMailSender javaMailSender;

	@Value("${spring.mail.username}")
	private String sender;

	EmailDetails emailDetails = new EmailDetails();

	public String sendMail(Student student) {
		emailDetails.setRecipient(student.getEmail());
		emailDetails.setMessage("Hello! "+student.getFirstName()+" You Are Successfully Registered on The Student Portal"+
				"\nAnd Your Roll Number is "+student.getRollNo());
		emailDetails.setSubject("Registration Successful");
		try {
			SimpleMailMessage smm= new SimpleMailMessage();

			smm.setFrom(sender);
			smm.setTo(emailDetails.getRecipient());
			smm.setText(emailDetails.getMessage());
			smm.setSubject(emailDetails.getSubject());

			javaMailSender.send(smm);
			return "Mail Sent Successfully";
		}
		catch (Exception e) {
			// TODO: handle exception
			return "Error While Sending Mail";
		}

	}

	public String sendMailToAll() { 
		//		for(int i=0; i<emArr.length; i++) {
		//			System.out.println(emArr[i]);
		//		}
		//		String[] arr= emArr.split(","); code for multiple email in single l	ine
		//		String reg="^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
		//		for(int i=0; i<arr.length; i++) {
		//			System.out.println(arr[i]);
		//		}

		//		for(int i=0; i<emArr.length(); i++) {
		//			for(int j=1; j<emArr.length(); j++) {
		//				if(emArr.substring(i, j).matches(reg)) {
		//					arr[0]=emArr.substring(i, j);
		//				}
		//			}	
		//		}

		String[] emArr=studentRepository.getAllStudsMail();
		emailDetails.setMessage("<b><h1 style=\"color:blue;\">HAPPY DIWALI TO YOU AND YOUR FAMILY DEAR STUDENT.</h1></b>");
		emailDetails.setSubject("DIWALI WISH");
		emailDetails.setAttachment("C:\\Users\\ts\\Downloads\\diwali-images-funkylife.jpg");

		MimeMessage mimeMessage= javaMailSender.createMimeMessage();

		MimeMessageHelper mimemessageHelper;

		try {
			// building email
			mimemessageHelper= new MimeMessageHelper(mimeMessage, true);
			mimemessageHelper.setFrom(sender);
			mimemessageHelper.setTo(emArr);
			mimemessageHelper.setText(emailDetails.getMessage(),true);
			mimemessageHelper.setSubject(emailDetails.getSubject());
			//Adding file And Setting Path
			FileSystemResource file = new FileSystemResource(new File(emailDetails.getAttachment()));
			mimemessageHelper.addAttachment(file.getFilename(), file);

			javaMailSender.send(mimeMessage);

			return "Mails Sent";
		}
		catch (Exception e) {
			return "Error occured While Sending Mails";	
		}
	}
}
