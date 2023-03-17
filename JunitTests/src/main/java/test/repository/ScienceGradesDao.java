package test.repository;

import org.springframework.data.repository.CrudRepository;

import test.model.ScienceGrade;

public interface ScienceGradesDao extends CrudRepository <ScienceGrade, Integer> {
	
	public Iterable <ScienceGrade> findGradeByStudentId (int id);
	
	public void deleteStudentById (int id);

}
