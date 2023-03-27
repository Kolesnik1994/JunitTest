package test.other;

import org.springframework.stereotype.Component;

import test.model.Student;

/**
 * Simple POJO java class that represent entity
 *  that have been tested in test/java/test/DemoUtilTest.java, JunitTestsApplicationTests, MockTest, ReflectionTestUtils
 */
@Component
public class CollegeStudent implements Student {
	
	private int id;

	private String firstname;
	
	private String lastname;

	private String emailAddress;
	
	private StudentGrades studentGrade;
	
	public CollegeStudent() {
	}

	public CollegeStudent(String firstname, String lastname, String emailAddress, StudentGrades studentGrade) {
		this.firstname = firstname;
		this.lastname = lastname;
		this.emailAddress = emailAddress;
		this.studentGrade = studentGrade;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public StudentGrades getStudentGrade() {
		return studentGrade;
	}

	public void setStudentGrade(StudentGrades studentGrade) {
		this.studentGrade = studentGrade;
	}
	
	@Override
	public String toString() {
		return "CollegeStudent [firstname=" + firstname + ", lastname=" + lastname + ", emailAddress=" + emailAddress
				+ ", studentGrade=" + studentGrade + "]";
	}

	@Override
	public String studentInformation() {
		return getFullName() + " " + getEmailAddress();
	}

	@Override
	public String getFullName() {
		return getFirstname() + "" + getLastname();
	}
	
	// private method which is being testing in /test/java/test/ReflectionTestUtils.java class 
	private String getFirstNameAndId() {
		return getFirstname() + "" + getId();	
		}

}
