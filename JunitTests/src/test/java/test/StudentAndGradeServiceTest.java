package test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import test.model.CollegeStudent2;
import test.repository.StudentDao;
import test.service.StudentAndGradeService;

@TestPropertySource("/application.properties")
@SpringBootTest(classes = JunitTestsApplication.class)
public class StudentAndGradeServiceTest {

	@Autowired
	private JdbcTemplate jdbc;

	@Autowired
	StudentAndGradeService studentService;

	@Autowired
	StudentDao studentDao;

	// before each test execute SQL-request to insert data in student table
	@BeforeEach
	public void setupDatabse() {
		jdbc.execute("insert into student (id, firstname, lastname, email_address)"
				+ "values (1, 'Eric', 'Robbys', 'eric@gmail.com')");
	}

	/**
	 * crate student with the service method, find student by email 
	 * assert the expected and actual email
	 */
	@Test
	public void createStudentService() {
		studentService.createStudent("Chad", "Darby", "chad@gmail.com");
		CollegeStudent2 student = studentDao.findByEmailAddress("chad@gmail.com");
		Assertions.assertEquals("chad@gmail.com", student.getEmailAddress(), "find by email");
	}

	/**
	 * remember that we execute SQL-request before each test
	 * checkIfStudentIsNull -> method that find student by Id and return true if student Present or false
	 * assertTrue-> if a student with id 1 exist return true, assertFalse if a student with id 0 does not exist return false 
	 */
	@Test
	public void isStudentNullCheck() {
		Assertions.assertTrue(studentService.checkIfStudentIsNull(1));
		Assertions.assertFalse(studentService.checkIfStudentIsNull(0));
	}

	/**
	 * remember that we execute SQL-request before each test
	 * checkIfStudentIsNull -> method that find student by Id and return true if student Present or false
	 * assertTrue-> if a student with id 1 exist return true, assertFalse if a student with id 0 does not exist return false 
	 */
	@Test
	public void deleteStudentService() {
		Optional<CollegeStudent2> deleteCollegeStudent = studentDao.findById(1);
		Assertions.assertTrue(deleteCollegeStudent.isPresent(), "Return True");
		studentService.deleteStudent(1);

		deleteCollegeStudent = studentDao.findById(1);
		Assertions.assertFalse(deleteCollegeStudent.isPresent(), "Return False");
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

		Assertions.assertEquals(5, collegeStudents.size());
	}

	// after each test execute SQL-request to delete data from student table
	@AfterEach
	public void setupAfterTransaction() {
		jdbc.execute("delete from student");
	}
	
	    //org.springframework.dao.DataIntegrityViolationException: could not execute statement; SQL [n/a]; constraint [student.PRIMARY]
		//Caused by: org.hibernate.exception.ConstraintViolationException: could not execute statement
		//Caused by: java.sql.SQLIntegrityConstraintViolationException: Duplicate entry '1' for key 'student.PRIMARY'
		

}
