package test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import test.model.CollegeStudent2;
import test.model.GradebookCollegeStudent;
import test.model.HistoryGrade;
import test.model.MathGrade;
import test.model.ScienceGrade;
import test.repository.HistoryDao;
import test.repository.MathGradesDao;
import test.repository.ScienceGradesDao;
import test.repository.StudentDao;
import test.service.StudentAndGradeService;

@TestPropertySource("/application-test.properties")
@SpringBootTest(classes = JunitTestsApplication.class)
public class StudentAndGradeServiceTest {

	@Autowired
	private JdbcTemplate jdbc;

	@Autowired
	StudentAndGradeService studentService;

	@Autowired
	StudentDao studentDao;
	
	@Autowired 
	MathGradesDao mathGradeDao;
	
	@Autowired
	ScienceGradesDao scienceGradeDao;
	
	@Autowired
	HistoryDao historyDao;
	// SQL scripts located in application.properties
	@Value ("${sql.scripts.create.student}")
	private String sqlAddStudent;
	
	@Value ("${sql.scripts.create.math.grade}")
	private String sqlCreateMathGrade;
	
	@Value ("${sql.scripts.create.science.grade}")
	private String sqlCreateScienceGrade;
	
	@Value ("${sql.scripts.create.history.grade}")
	private String sqlCreateHistoryGrade;
	
	@Value ("${sql.scripts.delete.student}")
	private String sqlDeleteStudent;
	
	@Value ("${sql.scripts.delete.math.grade}")
	private String sqlDeleteMathGrade;
	
	@Value ("${sql.scripts.delete.science.grade}")
	private String sqlDeleteScienceGrade;
	
	@Value ("${sql.scripts.delete.history.grade}")
	private String sqlDeleteHistoryGrade;
	
	// before each test execute SQL-request to insert data in student table
	@BeforeEach
	public void setupDatabse() {
		jdbc.execute(sqlAddStudent);        // or jdbc.execute("insert into student (id, firstname, lastname, email_address) values (1, 'Eric', 'Robbys', 'eric@gmail.com')");
		jdbc.execute(sqlCreateMathGrade);    // or jdbc.execute("insert into math_grade (id, student_id, grade) values (1, 1, 100.00)");
		jdbc.execute(sqlCreateHistoryGrade); // or jdbc.execute("insert into science_grade (id, student_id, grade) values (1, 1, 100.00)");
		jdbc.execute(sqlCreateScienceGrade); // or jdbc.execute("insert into history_grade (id, student_id, grade) values (1, 1, 100.00)");		
	}

	/**
	 * crate student with the service method, find student by email 
	 * assert the expected and actual email
	 */
	@Test
	public void createStudentService() {
		CollegeStudent2 student = new CollegeStudent2 ("Chad", "Darby", "chad@gmail.com");
		student.setId(12);
		studentDao.findByEmailAddress("chad@gmail.com");
		assertEquals("chad@gmail.com", student.getEmailAddress(), "find by email");
	}

	/**
	 * remember that we execute SQL-request before each test
	 * checkIfStudentIsNull -> method that find student by Id and return true if student Present or false
	 * assertTrue-> if a student with id 1 exist return true, assertFalse if a student with id 0 does not exist return false 
	 */
	@Test
	public void isStudentNullCheck() {
		assertTrue(studentService.checkIfStudentIsNull(1));
		assertFalse(studentService.checkIfStudentIsNull(0));
	}
	
	@Test
	public void deleteStudentService() {
		Optional <CollegeStudent2> deleteCollegeStudent = studentDao.findById(1);
		Optional <MathGrade> deleteMathGrade = mathGradeDao.findById(1);
		Optional <ScienceGrade> deleteScienceGrade = scienceGradeDao.findById(1);
		Optional <HistoryGrade> deleteHistoryGrade = historyDao.findById(1);
		
		assertTrue (deleteCollegeStudent.isPresent(), "Return True");
		assertTrue (deleteMathGrade.isPresent());
		assertTrue (deleteScienceGrade.isPresent());
		assertTrue (deleteHistoryGrade.isPresent());
		
		studentService.deleteStudent(1);

		deleteCollegeStudent = studentDao.findById(1);
		deleteMathGrade = mathGradeDao.findById(1);
		deleteScienceGrade = scienceGradeDao.findById(1);
		deleteHistoryGrade = historyDao.findById(1);
		
		assertFalse (deleteCollegeStudent.isPresent(), "Return False");
		assertFalse (deleteMathGrade.isPresent());
		assertFalse (deleteScienceGrade.isPresent());
		assertFalse (deleteHistoryGrade.isPresent());
	}

	/**
	 *  SQL ("/insertData.sql) insert 4 students in student table + 1 student from BeforeEach = 5
	 *  get all student, iterable CollegeStudent2 List and grab each student into my list
	 *  AssertEquals -> assert how many student i expected to find and equals with actual
	 */
	@Sql ("/insertData.sql")
	@Test
	public void getGradebookService() {
		Iterable<CollegeStudent2> iterableCollegeStudents = studentService.getGradebook();
		List<CollegeStudent2> collegeStudents = new ArrayList<>();
		for (CollegeStudent2 collegeStudent : iterableCollegeStudents) {
			collegeStudents.add(collegeStudent);
		}

		assertEquals(5, collegeStudents.size());
	}
	
	@Test
	public void createGradeService () {
		
		// Create the grade
		assertTrue(studentService.createGrade(80.50, 1, "math"));
		assertTrue (studentService.createGrade (80.50, 1, "science"));
		assertTrue (studentService.createGrade (80.50, 1, "history"));
		
		// get all grades with studentID
		Iterable <MathGrade> mathGrade = mathGradeDao.findGradeByStudentId(1);
		Iterable <ScienceGrade> scienceGrade = scienceGradeDao.findGradeByStudentId(1);
		Iterable <HistoryGrade> historyGrade = historyDao.findGradeByStudentId(1);
		
		
		// verify is there is grade
		assertTrue (mathGrade.iterator().hasNext(), "Student has math grades");
		assertTrue (scienceGrade.iterator().hasNext());
		assertTrue (historyGrade.iterator().hasNext());	
	}
	
	@Test
	public void createGradeServiceReturnFalsse() {
		assertFalse (studentService.createGrade(105, 1, "math"));
		assertFalse (studentService.createGrade (-5, 1, "math"));
		assertFalse (studentService.createGrade(80.50, 2, "math"));
		assertFalse (studentService.createGrade(80.50, 1, "literature"));
	}

	@Test
	public void deleteGradeService() {
		assertEquals (1,studentService.deleteGrade (1,"math"), "Returns student id after delete");
		assertEquals (1,studentService.deleteGrade (1,"science"), "Returns student id after delete");
		assertEquals (1,studentService.deleteGrade (1,"history"), "Returns student id after delete");
	}
	
	@Test 
	public void deleteNonExistStudent () {
		assertEquals (0, studentService.deleteGrade(0, "science"));
		assertEquals (0, studentService.deleteGrade(0, "literatur"));
		
	}
	
	@Test
	public void studentInformation() {		
		GradebookCollegeStudent gradebook = studentService.studentInformation(1);
		assertNotNull (gradebook);
		assertEquals (1, gradebook.getId());
		assertEquals ("Eric", gradebook.getFirstname());
		assertEquals ("Robbys", gradebook.getLastname());
		assertEquals ("eric@gmail.com", gradebook.getEmailAddress());
		assertTrue (gradebook.getStudentGrades().getMathGradeResults().size()==1);
		assertTrue (gradebook.getStudentGrades().getMathGradeResults().size()==1);		
	}
	
	// studentId(0) = does not exist;
	@Test
	public void studentInformationNull () {
		GradebookCollegeStudent gradebook = studentService.studentInformation(0);
		assertNull (gradebook);
		
	}
	
	// after each test execute SQL-request to delete data from student table
	@AfterEach
	public void setupAfterTransaction() {
		jdbc.execute (sqlDeleteStudent);   // or jdbc.execute ("delete from student");
		jdbc.execute (sqlDeleteMathGrade); // or jdbc.execute ("delete from math_grade");
		jdbc.execute (sqlDeleteScienceGrade );  //or jdbc.execute ("delete from science_grade");
		jdbc.execute (sqlDeleteHistoryGrade);  // or jdbc.execute ("delete from history_grade");
	}
	
		
}
