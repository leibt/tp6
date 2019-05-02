package dao;

import static dao.DAOUtilities.closures;
import static dao.DAOUtilities.initPrepQuery;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

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
		ResultSet result = null;
				
		String query = "INSERT INTO Orders (id_client, orderDate, total, paymentMethod, paymentStatus, deliveryMethod, deliveryStatus) VALUES (?, ?, ?, ?, ?, ?, ?)";
		try {
			cnx = daoFactory.getConnection();
			ps = initPrepQuery(cnx, query,true,
					order.getClient().getId(),
					new Timestamp(order.getDate().getMillis()),
					order.getTotal(),
					order.getPaymentMethod(),
					order.getPaymentStatus(),
					order.getDeliveryMethod(),
					order.getDeliveryStatus());
			
			int status = ps.executeUpdate();
			
			if(status == 0) {
				throw new DAOException("Failure to create the order");
			}
			
			result = ps.getGeneratedKeys();
			if(result.next()) {
				order.setId(result.getLong(1));
			}else {
				throw new DAOException("Failure to create the order");
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new DAOException(e);
		}finally {
			closures(cnx,ps,result);
		}
		
	}

	@Override
	public Order readByID(Long id) throws DAOException {
		Connection cnx = null;
		PreparedStatement ps = null;
		ResultSet result = null;
		Order order = null;
		
		String query = "SELECT id, id_client, orderDate, total, paymentMethod, paymentStatus, deliveryMethod, deliveryStatus FROM Orders WHERE id = ?";
		
		try {
			cnx = daoFactory.getConnection();
			ps = initPrepQuery(cnx, query, false, id);
			
			result = ps.executeQuery();
			
			if(result.next()) {
				order = getPropertiesOrder(result);
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
			throw new DAOException(e);
		}finally {
			closures(cnx,ps,result);
		}
		return order;
	}

	@Override
	public List<Order> readAll() throws DAOException {
		Connection cnx = null;
		PreparedStatement ps = null;
		ResultSet result = null;
		Order order = null;
		List<Order> ordersList = new ArrayList<>();
		
		String query = "SELECT id,id_client,orderDate,total,paymentMethod,paymentStatus,deliveryMethod,deliveryStatus FROM Orders";
		
		try {
			cnx = daoFactory.getConnection();
			ps = initPrepQuery(cnx, query, false);
			
			result = ps.executeQuery();
			
			while(result.next()) {
				order = getPropertiesOrder(result);
				ordersList.add(order);
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
			throw new DAOException(e);
		}finally {
			closures(cnx, ps, result);
		}
		
		return ordersList;
	}

	@Override
	public void delete(Order order) throws DAOException {
		Connection cnx = null;
		PreparedStatement ps = null;
		String query = "DELETE FROM Orders WHERE id = ?";
		
		try {
			cnx = daoFactory.getConnection();
			ps = initPrepQuery(cnx, query, false, order.getId());
			
			int status = ps.executeUpdate();
			
			if(status == 0) {
				throw new DAOException("Impossible to remove this order");
			}else {
				order.setId(null);
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			closures(cnx,ps);
		}
		
	}
	
	private Order getPropertiesOrder(ResultSet rs) throws SQLException{
		Order order = new Order();
		ClientDAO clientDAO = daoFactory.getClientDAO();
		
		order.setId(rs.getLong("id"));	    
	    //order.setDate( new DateTime( rs.getTimestamp("orderDate") ) );
	    order.setTotal(rs.getDouble("total"));
	    order.setPaymentMethod(rs.getString("paymentMethod"));
	    order.setPaymentStatus(rs.getString("paymentStatus"));
	    order.setDeliveryMethod(rs.getString("deliveryMethod"));
	    order.setDeliveryStatus(rs.getString("deliveryStatus"));
	    try {
	    	order.setClient( clientDAO.readByID( rs.getLong("id_client") ) );
		}catch(DAOException e) {
			throw new DAOException(e);
		}    
		
	    return order;
	}
	
}
