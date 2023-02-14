package test.model;

/**
 * Simple POJO java class that represent entity
 */
public class CollegeStudent implements Student {
	
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

}
