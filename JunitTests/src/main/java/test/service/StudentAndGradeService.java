package test.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import test.model.CollegeStudent2;
import test.repository.StudentDao;

@Service
@Transactional 
public class StudentAndGradeService {
	
	@Autowired
	private StudentDao studentDao;

	public void createStudent(String firstname, String lastname, String emailAddress) {
		
		CollegeStudent2 student = new CollegeStudent2 (firstname, lastname, emailAddress);
		//student.setId(1); 
		studentDao.save(student);
	}
	
	public boolean checkIfStudentIsNull (int id) {
		Optional <CollegeStudent2> student = studentDao.findById(id);
		if (student.isPresent()) {
			return true;
		}
			return false;
		}
	
	public void deleteStudent (int id) {
		if (checkIfStudentIsNull(id)) {
			studentDao.deleteById(id);
		}
		
	  }

	public Iterable<CollegeStudent2> getGradebook() {
		Iterable <CollegeStudent2> collegeStudent = studentDao.findAll();
		return collegeStudent;
	}
	
	}


