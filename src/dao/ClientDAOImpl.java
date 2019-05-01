package dao;

import static dao.DAOUtilities.closures;
import static dao.DAOUtilities.initPrepQuery;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import beans.Client;
import exceptions.DAOException;

public class ClientDAOImpl implements ClientDAO{

	private DAOFactory daoFactory;
	
	
	ClientDAOImpl(DAOFactory daoFactory){
		this.daoFactory = daoFactory;
	}
	
	@Override
	public void create(Client client) throws DAOException {
		Connection cnx = null;
		PreparedStatement ps = null;
		ResultSet result = null;
		
		String query = "INSERT INTO Client (name,firstname,address,phone,email,image) VALUES (?, ?, ?, ?, ?, ?)";
		
		try {
			cnx = daoFactory.getConnection();
			ps = initPrepQuery(cnx, query,true, client.getName(), client.getFirstname(), client.getAddress(), client.getPhone(),client.getEmail(),client.getImage());
		
			int statut = ps.executeUpdate();
			
			if(statut == 0 ) {
				throw new DAOException("Failure to create the client");
			}
			
			result = ps.getGeneratedKeys();
			if(result.next()) {
				client.setId(result.getLong(1));
			}else {
				throw new DAOException("Failure to create the client - No id returning");
			}
		}catch(SQLException e) {
			throw new DAOException(e);
		}finally {
			closures(result,ps,cnx);
		}
	}
	

	@Override
	public void delete(Client client) throws DAOException {
		Connection cnx = null;
		PreparedStatement ps = null;
		
		String query = "DELETE FROM Client WHERE id = ?";
		
		try {
			cnx = daoFactory.getConnection();
			ps = initPrepQuery(cnx,query,true,client.getId());
			
			int statut = ps.executeUpdate();
			if(statut == 0) {
				throw new DAOException("Failure to remove the client");
			}else {
				client.setId(null);
			}
						
		}catch(SQLException e) {
			throw new DAOException(e);
		}finally {
			closures(cnx,ps);
		}
		
		
	}

	@Override
	public List<Client> readAll() throws DAOException {
		Connection cnx = null;
		PreparedStatement ps = null;
		ResultSet result = null;
		
		Client client = null;
		List<Client> clientList = new ArrayList<>();
		
		String query = "SELECT id,name,firstname,address,phone,email,image FROM Client";
		
		try {
			cnx = daoFactory.getConnection();
			ps = initPrepQuery(cnx,query,false);
			
			result = ps.executeQuery();
			
			while(result.next()) {
				client = getPropertiesClient(result);
				clientList.add(client);
			}
		}catch(SQLException e) {
			throw new DAOException(e);
		}
		
		return clientList;
		
	}

	@Override
	public Client readByID(Long id) throws DAOException {
		Connection cnx = null;
		PreparedStatement ps = null;
		ResultSet result = null;
		Client client = null;
		
		String query = "SELECT id,name,firstname,address,phone,email,image FROM Client WHERE id = ?";
		
		try {
			cnx = daoFactory.getConnection();
			ps = initPrepQuery(cnx,query,false,id);
			
			result = ps.executeQuery();
			
			if(result.next()) {
				client = getPropertiesClient(result);
			}
		}catch(SQLException e) {
			throw new DAOException(e);
		}finally {
			closures(result,ps,cnx);
		}

		return client;
	}
	
	private static Client getPropertiesClient (ResultSet result) throws SQLException{
		Client client = new Client();
		
		client.setId(result.getLong("id"));
		client.setName(result.getString("name"));
		client.setFirstname(result.getString("firstname"));
		client.setAddress(result.getString("address"));
		client.setPhone(result.getString("phone"));
		client.setEmail(result.getString("email"));
		client.setImage(result.getString("image"));
		
		return client;
	}
	

}
