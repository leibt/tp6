package dao;

import java.util.List;

import beans.Client;
import exceptions.DAOException;

public interface ClientDAO {
	
	void create(Client client) throws DAOException;
	Client readByID(Long id) throws DAOException;
	List<Client> readAll() throws DAOException;
	void delete(Client client) throws DAOException;
}
