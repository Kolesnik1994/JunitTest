package test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

import test.dao.ApplicationDao;
import test.model.CollegeStudent;
import test.service.ApplicationService;

// Main application class
@SpringBootApplication
public class JunitTestsApplication {

	public static void main(String[] args) {
		SpringApplication.run(JunitTestsApplication.class, args);
	}
	
	//Bean which is used in JunitTestsApplicaionTests.java class special to assert that expected and actual object do not refer to the same object 
	@Bean(name ="collegeStudent")
	@Scope(value ="prototype")
	 CollegeStudent getCollegeStudent () {
		return new CollegeStudent();}
	
    @Bean (name ="applicationExample")
    ApplicationService getApplicationService () { return new ApplicationService();}
	
    @Bean (name ="applicationDao")
    ApplicationDao getApplicationDao() { return new ApplicationDao();}
    
    
}

