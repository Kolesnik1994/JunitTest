package test;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
import test.repository.StudentDao;
import test.service.StudentAndGradeService;

@TestPropertySource ("/application.properties")
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
	
	@BeforeAll
	public static void setud() {
		request = new MockHttpServletRequest();
		request.setParameter ("firstname", "Chad");
		request.setParameter ("lastname", "Bronson");
		request.setParameter ("emailAddress", "chad@gmail.com");
	}
	
	@BeforeEach
	public void beforeEach() {
		jdbc.execute ("insert into student (id, firstname, lastname, email_address)"
				+ "values (21, 'Eric', 'Robbys', 'eric@gmail.com')");
	}
	
	
	// Assert that expected iterables studentList and actual are equals, when we use studetnService to find them all;
	@Test
	public void getStudentsHttpRequest() throws Exception {
		
		CollegeStudent2 studentOne = new GradebookCollegeStudent ("Eric", "Robbys", "eric@gmail.com");	
		CollegeStudent2 studentTwo	= new GradebookCollegeStudent ("Chad", "Manson", "chad@gmail.com");
		List <CollegeStudent2> collegeStudentList = new ArrayList<> (Arrays.asList(studentOne,studentTwo));
		
		when(studentService.getGradebook()).thenReturn(collegeStudentList);
		Assertions.assertIterableEquals(collegeStudentList, studentService.getGradebook());
		
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
		Assertions.assertEquals (collegeStudentList, studentService.getGradebook());
		
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
		
		Assertions.assertTrue (studentDao.findById(21).isPresent());
		MvcResult mvcResult = mock.perform(MockMvcRequestBuilders.get("/delete/student/{id}", 21)).andExpect(status().isOk()).andReturn();
		ModelAndView mav = mvcResult.getModelAndView();
		ModelAndViewAssert.assertViewName(mav, "index");
		
		Assertions.assertFalse(studentDao.findById(21).isPresent());
	}
	
	@Test
	public void deleteStudentHttpRequestErrorPage() throws Exception {
		MvcResult mvcResult = mock.perform(MockMvcRequestBuilders.get("/delete/student/{id}", 0)).andExpect(status().isOk()).andReturn();
		
		ModelAndView mav = mvcResult.getModelAndView();
		ModelAndViewAssert.assertViewName(mav, "error");
		
	}
	
	@AfterEach
	public void setupAfterTransaction() {
		jdbc.execute("delete from student");
	}

}
