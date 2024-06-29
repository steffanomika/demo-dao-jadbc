package dbmodel.dao;

import java.util.List;

import dbmodel.entities.Department;

public interface DepartmentDao {

	void insert(Department obj);

	void delete(Department obj);

	void deleteById(Integer id);

	Department findById(Integer id);

	List<Department> findAll();
}
