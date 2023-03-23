package test.model;

import org.springframework.stereotype.Component;

/**
 * Student interface which displays information about student
 */
@Component
public interface Student {
	
	String studentInformation();
	
	String getFullName();

}
