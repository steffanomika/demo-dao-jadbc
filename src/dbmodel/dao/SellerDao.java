package dbmodel.dao;

import java.util.List;

import dbmodel.entities.Department;
import dbmodel.entities.Seller;

public interface SellerDao {

	void insert(Seller obj);

	void delete(Seller obj);

	void deleteById(Integer id);

	Seller findById(Integer id);

	List<Seller> findAll();
	
	List<Seller> findByDepartment(Department department); //achar vendedores por departamento
	
}
