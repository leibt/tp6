package dao;

import java.util.List;

import beans.Order;
import exceptions.DAOException;

public interface OrderDAO {

	void create(Order order) throws DAOException;
	Order readByID(Long id) throws DAOException;
	List<Order> readAll() throws DAOException;
	void delete(Order order) throws DAOException;
}
