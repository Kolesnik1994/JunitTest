package test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ch.qos.logback.core.model.Model;
import test.model.Gradebook;

@Controller
public class GradebookController {
	
	@Autowired
	private Gradebook gradebook;

	
	@RequestMapping (value ="/", method = RequestMethod.GET)
	public String getStudents (Model model) {
		return "index";
	}
	
	@GetMapping("/studentInformation/{id}")
	public String studentInfo (@PathVariable int id, Model model) {
		return "studentInformation";
	}
}
