package test.repository;

import org.springframework.data.repository.CrudRepository;

import test.model.MathGrade;

public interface MathGradesDao extends CrudRepository <MathGrade, Integer> {

	public Iterable <MathGrade> findGradeByStudentId (int id);
	
	public void deleteStudentById (int id);
}
