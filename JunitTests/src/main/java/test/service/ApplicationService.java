package test.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import test.dao.ApplicationDao;

public class ApplicationService {

	 @Autowired
	    private ApplicationDao applicationDao;

	    public double addGradeResultsForSingleClass(List<Double> numbers) {
	        return applicationDao.addGradeResultsForSingleClass(numbers);
	    }

	    public double findGradePointAverage (List<Double> grades ) {
	        return applicationDao.findGradePointAverage(grades);
	    }

	    public Object checkNull(Object obj) {
	        return applicationDao.checkNull(obj);
	    }
}
