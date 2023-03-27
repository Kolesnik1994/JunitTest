package test;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

import test.dao.ApplicationDao;
import test.model.CollegeStudent2;
import test.model.Grade;
import test.model.HistoryGrade;
import test.model.MathGrade;
import test.model.ScienceGrade;
import test.other.CollegeStudent;
import test.service.ApplicationService;

// Main application class
@SpringBootApplication 
public class JunitTestsApplication {

	public static void main(String[] args) {
		SpringApplication.run(JunitTestsApplication.class, args);
	}
	
	//Bean which is used in JunitTestsApplicaion.java class special to assert that expected and actual object do not refer to the same object 
	@Bean(name ="collegeStudent")
	@Scope(value ="prototype")
	 CollegeStudent getCollegeStudent1 () {
		return new CollegeStudent();}
	
	@Bean(name ="collegeStudent")
	@Scope(value ="prototype")
	 CollegeStudent2 getCollegeStudent () {
		return new CollegeStudent2();}
	
    @Bean (name ="applicationExample")
    ApplicationService getApplicationService () { return new ApplicationService();}
	
    @Bean (name ="applicationDao")
    ApplicationDao getApplicationDao() { return new ApplicationDao();}

	@Bean
	@Scope(value = "prototype")
	Grade getMathGrade(double grade) {
		return new MathGrade(grade);
	}

	@Bean
	@Scope(value = "prototype")
	@Qualifier("mathGrades")
	MathGrade getGrade() {
		return new MathGrade();
	}

	@Bean
	@Scope(value = "prototype")
	@Qualifier("scienceGrades")
	ScienceGrade getScienceGrade() {
		return new ScienceGrade();
	}

	@Bean
	@Scope(value = "prototype")
	@Qualifier("historyGrades")
	HistoryGrade getHistoryGrade() {
		return new HistoryGrade();
	}
    
    
}

