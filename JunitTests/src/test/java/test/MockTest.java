package test;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import ch.qos.logback.core.Context;
import test.dao.ApplicationDao;
import test.other.CollegeStudent;
import test.other.StudentGrades;
import test.service.ApplicationService;

/**
 * Java class that testing classes consist in test/other package
 */

@SpringBootTest
public class MockTest {

	@Autowired
	ApplicationContext contex;

	@Autowired
	CollegeStudent studentOne;

	@Autowired
	StudentGrades studentGrade;

	//Mock
	@MockBean
	private ApplicationDao applicationDao;
    
	//InjectMocks
	@Autowired
	private ApplicationService applicationService;

	@BeforeEach
	public void beforeEach() {
		studentOne.setFirstname("Paul");
		studentOne.setLastname("John");
		studentOne.setEmailAddress("paul@gmail.com");
		studentOne.setStudentGrade(studentGrade);
	}
	
	/** 
	 * when, applicationDao.addGradeResultsForSingleClass, then return 100. thenReturn is Mock response
	 * assertEquals -> equals 100 and application service that add grade results for a single class.
	 * verify the apllicationDao method was called, times is how many times this method was called
	 */
	@DisplayName ("When & Verify")
	@Test
	public void assertEqualsTestAdd () {
		when(applicationDao.addGradeResultsForSingleClass(studentGrade.getMathGradeResults())).thenReturn(100.00);
		
	    Assertions.assertEquals(100, applicationService.addGradeResultsForSingleClass(studentOne.getStudentGrade().getMathGradeResults()));;
	      
	    verify(applicationDao).addGradeResultsForSingleClass(studentGrade.getMathGradeResults());
	      
	    verify(applicationDao, times(1)).addGradeResultsForSingleClass(studentGrade.getMathGradeResults());
		
	}
	
	/*
	 * when, applicationDao.findGradePointAverage, then return 88.31. thenReturn is Mock response
	 * assertEquals -> equals 88.31 and application service that find grade point average. 
	 */
	@DisplayName ("Find Gpa")
	@Test
	public void assertEqualsTestFindGpa () {
		when(applicationDao.findGradePointAverage(studentGrade.getMathGradeResults())).thenReturn(88.31);
		
		Assertions.assertEquals (88.31, applicationService.findGradePointAverage(studentOne.getStudentGrade().getMathGradeResults()));
	}
	
	/*
	 * when, applicationDao.checkNull, then return true. thenReturn is Mock response
	 * assertNotNull -> application service check that object not null. 
	 */
	@DisplayName ("Not Null")
	@Test
	public void testAssertNotNull () {
		when(applicationDao.checkNull(studentGrade.getMathGradeResults())).thenReturn(true);
		
		Assertions.assertNotNull(applicationService.checkNull(studentOne.getStudentGrade().getMathGradeResults()), "Object should not be null");
	}
	
	/*
	 * retrieve "collegeStudent" bean from application context
	 * doThrow-> throw RuntimeException when method checkNull is called 
	 * assertThrows -> assert that the exception was thrown for that given method
	 */
	@DisplayName ("Throw run time Exception")
	@Test
	public void throwRunTimeException () {
		CollegeStudent nullStudent = (CollegeStudent) contex.getBean("collegeStudent");
		
		doThrow(new RuntimeException()).when(applicationDao).checkNull(nullStudent);
		
		Assertions.assertThrows(RuntimeException.class, ()-> {applicationService.checkNull(nullStudent);
			});
		
		verify(applicationDao, times(1)).checkNull(nullStudent);
	}
	
	/*
	 * retrieve "collegeStudent" bean from application context
	 * when we make call applicationDao check null, then throw exception or do not throw exception and return String
	 * assertThrow-> give exception, application service check null this is first call
	 * assertEquals -> we assert equals expected and actual string in second method checkNull that pass value
	 * verify is called Dao method two times
	 */
	@DisplayName ("Multiple Stubbing")
	@Test
	public void stubbingConsecutiveCalls () {
		CollegeStudent nullStudent = (CollegeStudent) contex.getBean("collegeStudent");
		
		when(applicationDao.checkNull(nullStudent)).thenThrow(new RuntimeException()).thenReturn("Do not throw Exception second time");
		
		Assertions.assertThrows (RuntimeException.class, ()-> {applicationService.checkNull(nullStudent);});
		
		Assertions.assertEquals ("Do not throw Exception second time", applicationService.checkNull(nullStudent));
		
		verify(applicationDao, times(2)).checkNull(nullStudent);
	}
  }






