package test.model;

public class GradebookCollegeStudent extends CollegeStudent2 {
	
	 private int id;

	    private StudentGrades2 studentGrades;

	    public GradebookCollegeStudent(String firstname, String lastname, String emailAddress) {
	        super(firstname, lastname, emailAddress);
	    }

	    public GradebookCollegeStudent(int id, String firstname, String lastname, String emailAddress, StudentGrades2 studentGrades) {
	        super(firstname, lastname, emailAddress);
	        this.studentGrades = studentGrades;
	        this.id = id;
	    }

	    public StudentGrades2 getStudentGrades() {
	        return studentGrades;
	    }

	    public void setStudentGrades(StudentGrades2 studentGrades) {
	        this.studentGrades = studentGrades;
	    }

	    @Override
	    public int getId() {
	        return id;
	    }

	    @Override
	    public void setId(int id) {
	        this.id = id;
	    }

}
