package test;


import java.util.ArrayList;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import test.other.CollegeStudent;
import test.other.StudentGrades;

/**
 * Java class that testing classes consist in test/other package
 * @author V.KOlESNIK
 */
@SpringBootTest
class JunitTestsApplicationTests {

	//static variable to count the number of times when the test method is called.
	private static int count = 0;
	
	//@Value - allows to work with values from properties files
	@Value ("${info.app.name}")
	private String appInfo;
	
	@Value ("${info.app.description}")
	private String appDescription;
	
	@Value ("${info.app.version}")
	private String appVersion;
	
	@Value ("${info.school.name}")
	private String schoolName;
	
	@Autowired
	CollegeStudent student;
	
	@Autowired
	StudentGrades studenGrades;
	
	@Autowired
	ApplicationContext context;
	
	//Executed before each test. Show properties values and count test
	@BeforeEach 
	public void beforeEach () {
		count = count +  1;
		System.out.println ("Testing: " + appInfo + "which is " + appDescription + " Version:" + appVersion + ".Execution of test method " + count);
		student.setFirstname("Eric");
		student.setLastname("Toplski");
		student.setEmailAddress("eric@gmail.com");	
		studenGrades.setMathGradeResults(new ArrayList<>(Arrays.asList(100.0, 85.0, 76.50, 91.75)));
		student.setStudentGrade(studenGrades);
	}
	
	// assert that expected and actual grade value equal
	@DisplayName ("Add grade result for student grades")
	@Test 
	public void addGradeResultForStudentGrades () {
		Assertions.assertEquals(353.25, studenGrades.addGradeResultsForSingleClass(student.getStudentGrade().getMathGradeResults()));	
	}
	
	// assert that expected and actual grade value are not equal
	@DisplayName ("Add grade result not equal")
	@Test
	public void addGradeResultForStudentNotEqaul() {
		Assertions.assertNotEquals(0, studenGrades.addGradeResultsForSingleClass(student.getStudentGrade().getMathGradeResults()));
		
	}
	
	// assert that actual value are greater than expected
	@DisplayName ("Is grade greater")
	@Test 
	public void isGradeGreaterStudentGrades () {
		Assertions.assertTrue (studenGrades.isGradeGreater(90, 75), "failure - should be true");
	}
	
	// assert that actual value are less than expected
	@DisplayName ("Is grade greater false")
	@Test 
	public void isGradeGreaterStudentGradesAssertFalse () {
		Assertions.assertFalse (studenGrades.isGradeGreater(89, 92), "failure - should be false");
	}
	
    // assert that student object not null
	@DisplayName ("Check Null for student grades")
	@Test 
	public void icheckNullForStudentGrades () {
		Assertions.assertNotNull (studenGrades.checkNull(student.getStudentGrade().getMathGradeResults()), "object - should not be null");
	}
	
	// assert that student info nut null, but studentGrade is null
	@DisplayName ("Create student without grade init")
	@Test 
	public void createStudentWithoutGradesInit () {
		CollegeStudent studentTwo = context.getBean("collegeStudent", CollegeStudent.class);
		studentTwo.setFirstname("Chad");
		studentTwo.setLastname("Darby");
		studentTwo.setEmailAddress("chad@gmail.com");
		Assertions.assertNotNull(studentTwo.getFirstname());
		Assertions.assertNotNull(studentTwo.getLastname());
		Assertions.assertNotNull(studentTwo.getEmailAddress());
		Assertions.assertNull(studenGrades.checkNull(studentTwo.getStudentGrade()));
	}
	
	//assert that student and studentTwo do not refer to the same object
	@DisplayName("Verify students are prototypes")
	@Test	
	public void verifyStudentsArePrototypes() {
	    CollegeStudent studentTwo = context.getBean("collegeStudent", CollegeStudent.class);
	    Assertions.assertNotSame(student, studentTwo);
	  }
	
	//assertAll -> allows us to testing multiple assertions 
	@DisplayName("Find Grade Point Average")
	@Test	
	public void findGradePointAverage() {
	    Assertions.assertAll("Testing all asseertEquals", ()-> assertEquals(353.25, 
	    		             studenGrades.addGradeResultsForSingleClass(student.getStudentGrade().getMathGradeResults())), 
	    		              ()-> assertEquals (88.31, studenGrades.findGradePointAverage(student.getStudentGrade().getMathGradeResults()))
	    		             ); 
	  }
	
	}


	



 

