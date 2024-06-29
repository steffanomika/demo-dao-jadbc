package application;

import java.util.Date;
import java.util.List;

import dbmodel.dao.DaoFactory;
import dbmodel.dao.SellerDao;
import dbmodel.entities.Department;
import dbmodel.entities.Seller;

public class Program {

	public static void main(String[] args) {
		
		SellerDao sellerDao = DaoFactory.createSellerDao();

		System.out.println("======================== TESTE 1 - Seller findByID ============================");
		Seller seller = sellerDao.findById(3);
		System.out.println(seller);

		System.out.println("\n======================== TESTE 2 - Seller findByDepartment ============================");
		Department department = new Department(2, null);
		List<Seller> list = sellerDao.findByDepartment(department); // referencia para o mesmo objeto (dep)
		for (Seller obj : list) {
			System.out.println(obj);
		}

		System.out.println("\n======================== TESTE 3 - Seller findByAll ============================");
		list = sellerDao.findAll();
		for (Seller obj : list) {
			System.out.println(obj);
		}

		System.out.println("\n======================== TESTE 4 - Seller Insert ============================");
		Seller newSeller = new Seller(null, "Greg", "greg@gmail.com", new Date(), 4000.0, department);
		sellerDao.insert(newSeller);
		System.out.println("Inserted new ID: " + newSeller.getId());
		
		System.out.println("\n======================== TESTE 5 - Seller Update ============================");
		seller = sellerDao.findById(1);
		seller.setName("Stéffano");
		//sellerDao.update(seller);
		//System.out.println("Update completed!");

	}

}
