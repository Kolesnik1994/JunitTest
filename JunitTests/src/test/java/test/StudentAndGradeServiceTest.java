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

	@BeforeEach
	public void setupDatabse() {
		jdbc.execute("insert into student (id, firstname, lastname, email_address)"
				+ "values (1, 'Eric', 'Robbys', 'eric@gmail.com')");
	}

	@Test
	public void createStudentService() {
		studentService.createStudent("Chad", "Darby", "chad@gmail.com");
		CollegeStudent2 student = studentDao.findByEmailAddress("chad@gmail.com");
		Assertions.assertEquals("chad@gmail.com", student.getEmailAddress(), "find by email");
	}

	@Test
	public void isStudentNullCheck() {

		Assertions.assertTrue(studentService.checkIfStudentIsNull(1));

		Assertions.assertFalse(studentService.checkIfStudentIsNull(0));
	}

	@Test
	public void deleteStudentService() {
		Optional<CollegeStudent2> deleteCollegeStudent = studentDao.findById(1);
		Assertions.assertTrue(deleteCollegeStudent.isPresent(), "Return True");
		studentService.deleteStudent(1);

		deleteCollegeStudent = studentDao.findById(1);
		Assertions.assertFalse(deleteCollegeStudent.isPresent(), "Return False");
	}

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

	@AfterEach
	public void setupAfterTransaction() {
		jdbc.execute("delete from student");
	}
}
