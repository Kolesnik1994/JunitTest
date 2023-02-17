package test;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.MethodMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AbstractContextLoader;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import test.model.CollegeStudent;
import test.model.StudentGrades;

@SpringBootTest (classes = JunitTestsApplication.class)
public class ReflectionTestUtils {
	
	
	@Autowired
	ApplicationContext context;
	
	@Autowired
	CollegeStudent studentOne;
	
	@Autowired
	StudentGrades studentGrade;
	
	// initialize student object
	@BeforeEach
	public void studentBeforeEach() {
	studentOne.setFirstname("Logan");
	studentOne.setLastname("Malkov");
	studentOne.setEmailAddress("logan@gmail.com");
	studentOne.setStudentGrade(studentGrade);
	
	//set private field directly
	org.springframework.test.util.ReflectionTestUtils.setField(studentOne, "id", 1);
	org.springframework.test.util.ReflectionTestUtils.setField(studentOne, "studentGrade", 
			new StudentGrades (new ArrayList<> (Arrays.asList(100.0, 85.0, 76.50, 91.75))));
	}
	
	// Equals value 1 and id that we set directly
	@Test
	public void getPrivateField() {
		Assertions.assertEquals (1, org.springframework.test.util.ReflectionTestUtils.getField(studentOne, "id"));
		
	}
	
	// Equals Logan1 and private method getFirstNameAndId that we called directly
	@Test
	public void invokePrivateMethod() {
	   Assertions.assertEquals("Logan1", org.springframework.test.util.ReflectionTestUtils.invokeMethod(studentOne, 
			   "getFirstNameAndId"), "Fail private method not call");
	}
}

		

	


