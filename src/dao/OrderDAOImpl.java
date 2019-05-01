package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import beans.Order;
import exceptions.DAOException;

public class OrderDAOImpl implements OrderDAO{
	
	private DAOFactory daoFactory;
	
	OrderDAOImpl(DAOFactory daoFactory){
		this.daoFactory = daoFactory;
	}
	
	@Override
	public void create(Order order) throws DAOException {
		Connection cnx = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		String query = "INSERT INTO Order (id_client, date, montant, modePaiement, statutPaiement, modeLivraison, statutLivraison) ";
		try {
			cnx = daoFactory.getConnection();
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public Order readByID(Long id) throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Order> readAll() throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(Order order) throws DAOException {
		// TODO Auto-generated method stub
		
	}
	
	private Order getPropertiesCommande(ResultSet rs) throws SQLException{
		Order order = new Order();
		order.setId(rs.getLong("id"));
	    //private Client client;
	    Date date = rs.getDate("date");
	    DateTime dateTime = new DateTime(date);
	    order.setDate(dateTime);
	    order.setTotal(rs.getDouble("montant"));
	    order.setPaymentMethod(rs.getString("modePaiement"));
	    order.setPaymentStatus(rs.getString("statutPaiement"));
	    order.setDeliveryMethod(rs.getString("modeLivraison"));
	    order.setDeliveryStatus(rs.getString("statutLivraison"));
	    
	    Long id_client = rs.getLong("id_client");
	    
		
	    return order;
	}
	
}
