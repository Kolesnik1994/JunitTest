package test.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import test.model.CollegeStudent2;
import test.model.Grade;
import test.model.GradebookCollegeStudent;
import test.model.HistoryGrade;
import test.model.MathGrade;
import test.model.ScienceGrade;
import test.model.StudentGrades2;
import test.repository.HistoryDao;
import test.repository.MathGradesDao;
import test.repository.ScienceGradesDao;
import test.repository.StudentDao;

@Service
@Transactional 
public class StudentAndGradeService {
	
	@Autowired
	private StudentDao studentDao;
	
	@Autowired
	@Qualifier("mathGrades")
	private MathGrade mathGrade;
	
	@Autowired
	@Qualifier("scienceGrades")
	private ScienceGrade scienceGrade;
	
	@Autowired
	@Qualifier("historyGrades")
	private HistoryGrade historyGrade;
	
	@Autowired
	private MathGradesDao mathGradeDao;
	
	@Autowired
	private ScienceGradesDao scienceGradeDao;
	
	@Autowired
	private HistoryDao historyGradeDao;
	
	@Autowired
	private StudentGrades2 studentGrades;
	
	
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
			mathGradeDao.deleteStudentById(id);
			scienceGradeDao.deleteStudentById (id);
			historyGradeDao.deleteStudentById(id);
		}	
		
	  }

	public Iterable<CollegeStudent2> getGradebook() {
		Iterable <CollegeStudent2> collegeStudent = studentDao.findAll();
		return collegeStudent;
	}
	
	public boolean createGrade (double grade, int studentId, String gradeType) {
		
		if (!checkIfStudentIsNull(studentId)) {
			return false;
		}
		
		if (grade >= 0 && grade <=100) {
			if (gradeType.equals("math")) {
				mathGrade.setId(0);
				mathGrade.setGrade(grade);
				mathGrade.setStudentId(studentId);
				mathGradeDao.save(mathGrade);
				return true;
			}
			if (gradeType.equals("science")) {
				scienceGrade.setId(0);
				scienceGrade.setGrade(grade);
				scienceGrade.setStudentId(studentId);
				scienceGradeDao.save(scienceGrade);
				return true;
			}
			
			if (gradeType.equals("history")) {
				historyGrade.setId(0);
				historyGrade.setGrade(grade);
				historyGrade.setStudentId(studentId);
				historyGradeDao.save(historyGrade);
				return true;
			}
		}
		
		return false;
	}
	
	public int deleteGrade (int id, String gradeType) {
		
		int studentId=1; // was studentId=0 l like in Tutorial
		
		if (gradeType.equals("math")) {
			Optional <MathGrade> grade = mathGradeDao.findById(id);	
			if (!grade.isPresent()) {
				return studentId;
			}
			studentId = grade.get().getStudentId();
			mathGradeDao.deleteById(id);
		}
		
		if (gradeType.equals("science")) {
			Optional <ScienceGrade> grade = scienceGradeDao.findById(id);
			if (!grade.isPresent()) {
				return studentId;
			}
			studentId = grade.get().getStudentId();
			scienceGradeDao.deleteById(id);
		}
		
		if (gradeType.equals("history")) {
			Optional <HistoryGrade> grade = historyGradeDao.findById(id);
			if (!grade.isPresent()) {
				return studentId;
			}
			studentId = grade.get().getStudentId();
			historyGradeDao.deleteById(id);
		}
		
	return studentId;
	}
	
	public GradebookCollegeStudent studentInformation (int id)  {
		
		if (!checkIfStudentIsNull(id)) {
			return null;
		}
		
		Optional <CollegeStudent2> student = studentDao.findById(id);
		Iterable <MathGrade> mathGrade = mathGradeDao.findGradeByStudentId(id);
		Iterable <ScienceGrade> scienceGrade = scienceGradeDao.findGradeByStudentId(id);
		Iterable <HistoryGrade> historyGrade = historyGradeDao.findGradeByStudentId(id);
		
		List <Grade> mathGradeList = new ArrayList<>();
		mathGrade.forEach(mathGradeList::add);
		
		List <Grade> scienceGradeList = new ArrayList<>();
		scienceGrade.forEach(scienceGradeList::add);
		
		List <Grade> historyGradeList = new ArrayList<>();
		historyGrade.forEach(historyGradeList::add);
		
		studentGrades.setMathGradeResults(mathGradeList);
		studentGrades.setScienceGradeResults(scienceGradeList);
		studentGrades.setHistoryGradeResults(historyGradeList);
		 
		GradebookCollegeStudent gradebook = new GradebookCollegeStudent 
				(student.get().getId(), student.get().getFirstname(), 
				student.get().getLastname(), student.get().getEmailAddress(), studentGrades);	
		 return gradebook;
	}
	
	}

   //org.springframework.dao.DataIntegrityViolationException: could not execute statement; SQL [n/a]; constraint [student.PRIMARY]
	//Caused by: org.hibernate.exception.ConstraintViolationException: could not execute statement
	//Caused by: java.sql.SQLIntegrityConstraintViolationException: Duplicate entry '1' for key 'student.PRIMARY'
	




