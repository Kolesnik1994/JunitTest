package test;

import java.time.Duration;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;


/*
 * Java class that testing method consist in DemoUtil.java class
 * @author V.KOLESNIK
 * 
 * @TestMethodOrder - allows you to set the order in which test will executed
 * MethodOrderer.OrderAnnotation.class -> test will executed in order that give annotation @Order(1)
 */
@TestMethodOrder (MethodOrderer.OrderAnnotation.class)
public class DemoUtilTest {
	
	DemoUtils demoUtils;
	
	 @BeforeEach
	 void setupBeforeEach() {
		 demoUtils = new DemoUtils();
		 System.out.println ("@BeforeEach executes before the execution of each test method");
	 }
	 
	 @AfterEach
	 void tearDownAfterEach () {
		 System.out.println ("Running @AfterEach");
	 }
	 
	 
	@Test
	@DisplayName ("Equals and not Equals")
	void testEqualsAndNotEquals () {
		
		System.out.println ("Running test: testEqualsAndNotEquals");
		
		Assertions.assertEquals(6, demoUtils.add(2, 4), "2+4 must be 6");
		Assertions.assertNotEquals (6, demoUtils.add(2, 5), "2+5 must be 7");
	}
		
	 /* assertNull - > testing that str1 should be null
	  * assertNotEquals -> testing str2 should not be null
	  */
	@Test
	@DisplayName ("Null and not Null")
	void testNullAndNotNull() {
		 
		System.out.println ("Running test: testNullAndNotNull");
		
		String str1	= null;
		String str2 = "luvCode";
		
		Assertions.assertNull (demoUtils.checkNull(str1), "Object should be null");
		Assertions.assertNotNull (demoUtils.checkNull(str2), "Object shoul be not null");
		
	}
	
	 /* assertTrue - > testing that int a will be greater than the number g
	  * assertNotEquals -> testing that int g will be less than the number g
	  */
	@Test
	@DisplayName ("True and False")
	void testSameAndNotSame() {
		
		int g = 2;
		int a = 5;
		
		Assertions.assertTrue(demoUtils.isGreater(a, g), "This should return true");
		Assertions.assertFalse(demoUtils.isGreater(g, a), "This should retrun false");
	}
	
	 /* assertArrayEquals - > testing that String[] stringArray equals String[] in method getFirstThreeLettersOfAlphabet
	  */
	@DisplayName ("Arrays Equals")
	@Test
	void testArrayEquals() {
	String[] stringArray = {"A", "B", "C"};	
	
	Assertions.assertArrayEquals (stringArray, demoUtils.getFirstThreeLettersOfAlphabet(), "Array should be same");
	}
	
	 /* assertIterableEquals - > testing that String[] theList iterables are deeply equals String[] in method getAcademyInList
	  */
	@DisplayName ("Iterable Equals")
	@Test
	void testIterableEquals() {
		List <String> theList= List.of("luv", "2", "code");
		
		Assertions.assertIterableEquals (theList, demoUtils.getAcademyInList(), "Expected list should be the same");
	}
	
	 /* Lines Match - > testing that String[] theList matches String[] in method getAcademyInList
	  */
	@DisplayName ("Lines Match")
	@Test
	void testLinesMatch() {
		List <String> theList= List.of("luv", "2", "code");
		
		Assertions.assertLinesMatch (theList, demoUtils.getAcademyInList(), "Lines should match");
	}
	
	 /* assertThrows - > execute Exception if number in method throwException less than 0 
	  * assertDoesNotThrow -> does not execute Exception if number in method throwException biggest than 0
	  */
	@DisplayName ("Throw and Does Not Throw Exception")
	@Test
	void testThrowException () {
		
		Assertions.assertThrows(Exception.class, ()-> {demoUtils.throwException(-1); }, "Should throw Exception");
		
		Assertions.assertDoesNotThrow (()-> {demoUtils.throwException (5);}, "Does Not Throw");
	}
	
	 /* assertTimeoutPreemptively - >  testing that method checkTimeout executed duration 3s
	  */
	@DisplayName ("Timeout")
	@Test
	void testTimeout () {
		Assertions.assertTimeoutPreemptively(Duration.ofSeconds(3), ()-> {demoUtils.checkTimeout();}, "Methoud should execute in 3 second");
	}
	
	 /*
	 @BeforeAll
	 static void setupBeforerAllClass () {
		 System.out.println ("@BeforeAll");
	 }
	 
	 @AfterAll
	 static void setupAfterAllClass() {
	 System.out.println ("@AfterAll");
	 }
    */
	 
	 
	 /* assertEquals - > testing that 6 equals sum of number in method add
	  * assertNotEquals -> testing that 6 not equals sum of number in method add 
	  */
	 
	 
	 // @Disabled allows you to skip test
	 @Test
	 @Order (1)
	 @Disabled ("Don't run until JIRA #123 is resolved")
	 void oldTest() {
		 
	 }
	 
	 // @EnabledOnOs - allows you executed test on a particular OS
	 @Test
	 @Order (2)
	 @EnabledOnOs  (OS.MAC)
	 void testForMac() {
		 
	 }
	
}
		
	
	


