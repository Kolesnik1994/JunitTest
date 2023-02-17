package test.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import test.model.CollegeStudent2;

@Repository
public interface StudentDao extends CrudRepository <CollegeStudent2, Integer> {

	public CollegeStudent2 findByEmailAddress(String emailAddress);
}
