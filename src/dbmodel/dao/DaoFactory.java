package dbmodel.dao;

import db.DB;
import dbmodel.dao.impl.SellerDaoJDBC;

public class DaoFactory {
	
	//classe auxiliar responsavel por instanciar os DAOS
	
	//retorna o tipo da interface, mas internamente instancia a implementação
	public static SellerDao createSellerDao() {
		return new SellerDaoJDBC(DB.getConnection()); //passando a conexão como argumento
	}

}
