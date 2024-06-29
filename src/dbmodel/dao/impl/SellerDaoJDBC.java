package dbmodel.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import dbmodel.dao.SellerDao;
import dbmodel.entities.Department;
import dbmodel.entities.Seller;

public class SellerDaoJDBC implements SellerDao {

	//o DAO tem uma dependencia com a conexão
	private Connection conn;

	//criando um construtor para forçar a injenção de dependência
	//e tera ele a disposição em qualquer lugar da classe
	public SellerDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public void insert(Seller obj) {

		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"INSERT INTO seller " 
					+ "(Name, Email, BirthDate, BaseSalary, DepartmentId) " 
					+ "VALUES "  
					+ "(?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS); //retorna o ID do novo obj
			
			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartment().getId());
			
			int linhas = st.executeUpdate();
			
			if(linhas > 0) { //se linhas alteradas maior que zero, foi inserido
				ResultSet rs = st.getGeneratedKeys(); //pegando o ID
				if(rs.next()) { //if pq apenas um dado, se existir, pega o ID gerado
					int id = rs.getInt(1); //1 coluna das chaves
					obj.setId(id); //populando o OBJ com o id gerado
				}
				DB.closeResultSet(rs);
			}else {
				throw new DbException("Erro! nenhuma linha alterada.");
			}
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void delete(Seller obj) {
		
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteById(Integer id) {
		// TODO Auto-generated method stub

	}

	@Override
	public Seller findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT seller.*,department.Name as DepName "  
					+ "FROM seller INNER JOIN department "  
					+ "ON seller.DepartmentId = department.Id  "
					+ "WHERE seller.Id = ?");
			
			//o ? vai ser o valor do Id do parâmetro
			st.setInt(1, id);
			rs = st.executeQuery(); //resultado da consulta cai no RS
			//quando faz a consulta, RS está apontando para posição zero
			if(rs.next()) { //testar se veio algum resultado, se nao, volta nullo, nao existe vendedor
				Department dep = instantiateDepartment(rs);
				Seller obj = instantiateSeller(rs, dep);
				return obj;
				
			}
			return null;
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st); //nao precisa fechar a conexão aqui
			DB.closeResultSet(rs);
		}

	}

	//fazendo instanciação dos objetos por métodos
	private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException {
		Seller obj = new Seller();
		obj.setId(rs.getInt("Id"));
		obj.setName(rs.getString("Name"));
		obj.setEmail(rs.getString("Email"));
		obj.setBirthDate(rs.getDate("BirthDate"));
		obj.setBaseSalary(rs.getDouble("BaseSalary"));
		obj.setDepartment(dep);
		return obj;
	}

	private Department instantiateDepartment(ResultSet rs) throws SQLException {
		Department dep = new Department();
		dep.setId(rs.getInt("DepartmentId"));
		dep.setName(rs.getString("DepName"));
		return dep;
	}

	@Override
	public List<Seller> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
				"SELECT seller.*,department.Name as DepName "   
				+ "FROM seller INNER JOIN department "
				+ "ON seller.DepartmentId = department.Id " 
				+ "ORDER BY Name");
							
			rs = st.executeQuery(); 
			
			List<Seller> list = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>();
			
			while(rs.next()) { //WHILE pq terá varios valores
				Department dep = map.get(rs.getInt("DepartmentId"));
				if(dep == null) { //controlando a não repetição de departamento
					dep = instantiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);
				}
				
				Seller obj = instantiateSeller(rs, dep);
				list.add(obj); //instancia e adiciona na lista apontando para o dep
				//dep com os vendedores apontando pra ele
				
			}
			return list;
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}

	}

	@Override
	public List<Seller> findByDepartment(Department department) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
				"SELECT seller.*,department.Name as DepName "   
				+ "FROM seller INNER JOIN department "
				+ "ON seller.DepartmentId = department.Id " 
				+ "WHERE DepartmentId = ? " 
				+ "ORDER BY Name");
							
			st.setInt(1, department.getId()); //id do departamento passado no argumento
			rs = st.executeQuery(); 
			
			List<Seller> list = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>();
			
			while(rs.next()) { //WHILE pq terá varios valores
				Department dep = map.get(rs.getInt("DepartmentId"));
				if(dep == null) {
					dep = instantiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);
				}
				
				Seller obj = instantiateSeller(rs, dep);
				list.add(obj); //instancia e adiciona na lista apontando para o dep
				//dep com os vendedores apontando pra ele
				
			}
			return list;
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}

	}

}
