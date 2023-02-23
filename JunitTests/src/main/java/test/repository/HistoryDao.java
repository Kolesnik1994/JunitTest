package test.repository;

import org.springframework.data.repository.CrudRepository;

import test.model.HistoryGrade;

public interface HistoryDao extends CrudRepository <HistoryGrade, Integer> {
	
	public Iterable <HistoryGrade> findGradeByStudentId (int id);

}
