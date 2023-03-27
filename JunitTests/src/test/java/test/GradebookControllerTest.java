package test;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.servlet.ModelAndView;

import test.model.CollegeStudent2;
import test.model.GradebookCollegeStudent;
import test.model.MathGrade;
import test.repository.MathGradesDao;
import test.repository.StudentDao;
import test.service.StudentAndGradeService;

@TestPropertySource ("/application-test.properties")
@AutoConfigureMockMvc
@SpringBootTest
public class GradebookControllerTest {
	
	private static MockHttpServletRequest request;
	
	@Autowired
	private JdbcTemplate jdbc;
	
	@Autowired	
	private MockMvc mock;
	
	@Autowired 
	private StudentDao studentDao;
	
	@Mock
	private StudentAndGradeService studentService;
	
	@Autowired
	private StudentAndGradeService studentServ;
	
	@Autowired
	private MathGradesDao mathGradesDao;
	
	@BeforeAll
	public static void setup() {
		request = new MockHttpServletRequest();
		request.setParameter ("firstname", "Chad");
		request.setParameter ("lastname", "Bronson");
		request.setParameter ("emailAddress", "chad@gmail.com");
	}
	
	@BeforeEach
	public void beforeEach() {
		jdbc.execute ("insert into student (id, firstname, lastname, email_address) values ('21', 'Eric', 'Robbys', 'eric@gmail.com')");
		jdbc.execute("insert into math_grade (id, student_id, grade) values (21,21,100.00)");
		jdbc.execute ("insert into science_grade (id, student_id, grade) values (21,21,100.00)");
		jdbc.execute("insert into history_grade (id, student_id, grade) values (21,21,100.00)");
			
	}
	
	// Assert that expected iterables studentList and actual are equals, when we use studetnService to find them all;
	@Test
	public void getStudentsHttpRequest() throws Exception {
		
		CollegeStudent2 studentOne = new GradebookCollegeStudent ("Eric", "Robbys", "eric@gmail.com");	
		CollegeStudent2 studentTwo	= new GradebookCollegeStudent ("Chad", "Manson", "chad@gmail.com");
		List <CollegeStudent2> collegeStudentList = new ArrayList<> (Arrays.asList(studentOne,studentTwo));
		
		when(studentService.getGradebook()).thenReturn(collegeStudentList);
		assertIterableEquals(collegeStudentList, studentService.getGradebook());
		
		/**
		 *  use mockMvc to perform web request for slash ("/") and then i expected status 202 that everything okay
		 *  get ModelandView in controller, and assert expected view name and actual ("index")
		 */
		MvcResult mvcResult = mock.perform(MockMvcRequestBuilders.get("/")).andExpect(status().isOk()).andReturn();
		ModelAndView mav = mvcResult.getModelAndView();
		ModelAndViewAssert.assertViewName(mav, "index");
				
	}
	
	@Test 
	public void createStudentHttpRequest() throws Exception {
		
		CollegeStudent2 studentOne = new CollegeStudent2 ("Eric", "Robbys", "eric@gmail.com");
		studentOne.setId(12);
		
		List <CollegeStudent2> collegeStudentList = new ArrayList<> (Arrays.asList(studentOne));
		when(studentService.getGradebook()).thenReturn(collegeStudentList);
		assertEquals (collegeStudentList, studentService.getGradebook());
		
		MvcResult mvcResult = this.mock.perform(post("/").contentType(MediaType.APPLICATION_JSON)
				.param ("firstname",request.getParameterValues("firstname"))
				.param ("lastname",request.getParameterValues("lastname"))
				.param ("emailAddress",request.getParameterValues("emailAddress")))
				.andExpect(status().isOk()).andReturn();
				
		ModelAndView mav = mvcResult.getModelAndView();
		ModelAndViewAssert.assertViewName(mav, "index");
		
		CollegeStudent2 verifyStudent = studentDao.findByEmailAddress("chad@gmail.com");
		Assertions.assertNotNull (verifyStudent, "Student should be found");
	}
	
	@Test
	public void deleteStudentHttpRequest() throws Exception {
		
		assertTrue (studentDao.findById(21).isPresent());
		
		MvcResult mvcResult = mock.perform(MockMvcRequestBuilders.get("/delete/student/{id}", 21)).andExpect(status().isOk()).andReturn();
		ModelAndView mav = mvcResult.getModelAndView();
		ModelAndViewAssert.assertViewName(mav, "index");
		
		assertFalse(studentDao.findById(21).isPresent());
	}
	
	@Test
	public void deleteStudentHttpRequestErrorPage() throws Exception {
		MvcResult mvcResult = mock.perform(MockMvcRequestBuilders.get("/delete/student/{id}", 0)).andExpect(status().isOk()).andReturn();	
		ModelAndView mav = mvcResult.getModelAndView();
		
		ModelAndViewAssert.assertViewName(mav, "error");
		
	}
	
	@Test
	public void studentInformationHttpRequest () throws Exception {
		
		assertTrue (studentDao.findById(21).isPresent());
		
		MvcResult mvcResult = mock.perform(MockMvcRequestBuilders.get("/studentInformation/{id}", 21)).andExpect(status().isOk()).andReturn();
		ModelAndView mav = mvcResult.getModelAndView();
		
		ModelAndViewAssert.assertViewName(mav, "studentInformation");
	}
	
	// studentId 0 does not exist
	@Test
	public void studentInformationHttpStudentDoesNotExistRequest() throws Exception {
		
		assertFalse(studentDao.findById(0).isPresent());
		
		MvcResult mvcResult = mock.perform (MockMvcRequestBuilders.get("/studentInformation/{id}", 0)).andExpect(status().isOk()).andReturn();
		ModelAndView mav = mvcResult.getModelAndView();
		
		ModelAndViewAssert.assertViewName (mav, "error");
	}
	
	@Test
	public void createValidGradeHttpRequest () throws Exception {
		
		assertTrue (studentDao.findById(21).isPresent());
		
		GradebookCollegeStudent student = studentServ.studentInformation(21);  // something wrong when service get Student Info
	
		assertEquals(1, student.getStudentGrades().getMathGradeResults().size());
		
		MvcResult mvcResult = this.mock.perform(post ("/grades").contentType (MediaType.APPLICATION_JSON)
				.param("grade", "85.00")
				.param("gradeType", "math")
				.param("studentId", "21" )).andExpect(status().isOk()).andReturn();
				
				ModelAndView mav = mvcResult.getModelAndView();
				ModelAndViewAssert.assertViewName(mav, "studentInformation");
				
				student = studentServ.studentInformation(21);
				assertEquals (2, student.getStudentGrades().getMathGradeResults().size());
	}
	
	// studentID 23 does not exist
	@Test
	public void createAValidGradeHttpRequestStudentIdDoesNotExist () throws Exception {
		MvcResult mvcResult = this.mock.perform(post("/grades").contentType(MediaType.APPLICATION_JSON)
				.param("grade", "85.00")
				.param("gradeType", "math")
				.param("studentId", "23" )).andExpect(status().isOk()).andReturn();
				
			ModelAndView mav = mvcResult.getModelAndView();
			
			ModelAndViewAssert.assertViewName(mav, "error");
	}
	
	//gradeType "literature" does not exist
	@Test
	public void createANonValidGradeHttpRequestGradeTypeDoesNotExist () throws Exception {
		MvcResult mvcResult = mock.perform(post("/grades").contentType(MediaType.APPLICATION_JSON)
				.param("grade", "85.00")
				.param("gradeType", "literature")
				.param("studentId", "23" )).andExpect(status().isOk()).andReturn();

		ModelAndView mav = mvcResult.getModelAndView();
		
		ModelAndViewAssert.assertViewName (mav, "error");
		
	}
	
	@Test
	public void deleteAValidGradeHttpRequest() throws Exception  {
		
		Optional <MathGrade> mathGrade = mathGradesDao.findById(21);
		assertTrue(mathGrade.isPresent());
		
		MvcResult mvcResult = mock.perform(MockMvcRequestBuilders.get("/grades/{id}/{gradeType}", 21, "math"))
				                                                 .andExpect(status().isOk()).andReturn();
		
		ModelAndView mav = mvcResult.getModelAndView();
		ModelAndViewAssert.assertViewName(mav, "studentInformation");
		
		mathGrade = mathGradesDao.findById(21);
		assertFalse(mathGrade.isPresent());
	}
	
	//studentId 22 does not exist
	@Test
	public void deleteValidGradeHttpRequestStudentIdDoesNotExist () throws Exception {
		
		Optional <MathGrade> mathGrade = mathGradesDao.findById(22);
		assertFalse(mathGrade.isPresent());
		
		MvcResult mvcResult = mock.perform(MockMvcRequestBuilders.get("/grades/{id}/{gradeType}", 22, "math"))
				                                                 .andExpect(status().isOk()).andReturn();
		ModelAndView mav = mvcResult.getModelAndView();
		ModelAndViewAssert.assertViewName (mav, "error");
		
	}
	
	@Test
	public void deleteANonValidGradHttpRequest() throws Exception {
		
		MvcResult mvcResult = mock.perform(MockMvcRequestBuilders.get("/grades/{id}/{gradeType}", 21, "literature"))
				                                          .andExpect(status().isOk()).andReturn();
				
		ModelAndView mav = mvcResult.getModelAndView();
		
		ModelAndViewAssert.assertViewName(mav, "error");
	}
		
				
	@AfterEach
	public void setupAfterTransaction() {
		jdbc.execute("delete from student");
		jdbc.execute("delete from math_grade");
		jdbc.execute("delete from science_grade");
		jdbc.execute("delete from history_grade");
	}


}




