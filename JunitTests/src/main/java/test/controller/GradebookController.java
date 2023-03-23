package test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import test.model.CollegeStudent2;
import test.model.Gradebook;
import test.model.GradebookCollegeStudent;
import test.service.StudentAndGradeService;

@Controller
public class GradebookController {
	
	@Autowired
	private Gradebook gradebook;
	
	@Autowired
	private StudentAndGradeService studentService;

	@RequestMapping (value ="/", method = RequestMethod.GET)
	public String getStudents (Model model) {
		Iterable <CollegeStudent2> collegeStudents = studentService.getGradebook();
		model.addAttribute("students", collegeStudents);
		return "index";
	}
	
	@PostMapping (value = "/")
	public String createStudent(@ModelAttribute("student") CollegeStudent2 student, Model model) {
		studentService.createStudent(student.getFirstname(), student.getLastname(), student.getEmailAddress());
		Iterable <CollegeStudent2> collegeStudents = studentService.getGradebook();
		model.addAttribute("students",collegeStudents);
		return "index";
		
	}
	
	@GetMapping ("/delete/student/{id}")
	public String deleteStudent (@PathVariable int id, Model model) {
		
		if (!studentService.checkIfStudentIsNull(id)) {
		return "error";
		}		
		studentService.deleteStudent(id);
		Iterable <CollegeStudent2> collegeStudent = studentService.getGradebook();
		model.addAttribute("students",collegeStudent);
		return "index";
	}
	
	@GetMapping("/studentInformation/{id}")
	public String studentInfo (@PathVariable int id, Model model) {
		
		if (!studentService.checkIfStudentIsNull(id)) {
			return "error"; }
		
		studentService.configureStudentInformation(id, model);
			
			return "studentInformation";
	}
	
	@PostMapping ("/grades")
	public String postGrades (@RequestParam ("grade") double grade, 
	                        @RequestParam ("studentId") int studentId,
	                        @RequestParam ("gradeType") String gradeType, Model model) {
		
		if (!studentService.checkIfStudentIsNull(studentId)) {
			return "error"; }

		boolean success = studentService.createGrade(grade, studentId, gradeType);
	
		if (!success) {
			return "error"; }
		
		studentService.configureStudentInformation(studentId, model);
		
		return "studentInformation";
	}
	
	@GetMapping ("/grades/{id}/{gradeType}")
	public String getGradesId (@PathVariable int id, @PathVariable String gradeType, Model model ) {
		
		int studentId = studentService.deleteGrade(id, gradeType);
		
		if (studentId == 0) {       
			return "error";
		}
                                
		studentService.configureStudentInformation(studentId, model);
			
		return "studentInformation";
		
	}
	
	
	
	
}
