package dao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;

import exceptions.DAOConfigException;

public class DAOFactory {
	
	private static final String PROPERTIES_FILE  	= "/config/dao.properties";
    private static final String PROPERTY_URL        = "url";
    private static final String PROPERTY_DRIVER     = "driver";
    private static final String PROPERTY_USERNAME 	= "username";
    private static final String PROPERTY_PASSWORD   = "password";

    private String url;
    private String username;
    private String password;
	
    BoneCP cnxPool = null;
    
    DAOFactory(BoneCP cnxPool){
    	this.cnxPool = cnxPool;
    }
    
    public DAOFactory(String url,String username,String password) {
    	this.url=url;
    	this.username=username;
    	this.password=password;
    }
    
    
    public static DAOFactory getInstance() throws DAOConfigException{
    	Properties properties = new Properties();
    	String url;
    	String driver;
    	String username;
    	String password;
    	
    	BoneCP cnxPool = null;
    	
    	ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    	InputStream propertiesFile = classLoader.getResourceAsStream(PROPERTIES_FILE);
    	
    	if(propertiesFile == null) {
    		throw new DAOConfigException("Properties file "+ propertiesFile + "doesn't exist");
    	}
    	
    	try {
    		properties.load(propertiesFile);
    		url = properties.getProperty(PROPERTY_URL);
    		driver = properties.getProperty(PROPERTY_DRIVER);
    		username = properties.getProperty(PROPERTY_USERNAME);
    		password = properties.getProperty(PROPERTY_PASSWORD);
    	}catch(FileNotFoundException e) {
    		throw new DAOConfigException("Properties file "+PROPERTIES_FILE+" is not found",e);
    	}catch(IOException e) {
    		throw new DAOConfigException("Impossible to load " + PROPERTIES_FILE);
    	}
    	
    	try {
    		Class.forName(driver);
    	}catch(ClassNotFoundException e ) {
    		throw new DAOConfigException("Driver not found ",e);
    	}
    	
    	try {
    		BoneCPConfig config = new BoneCPConfig();
    		config.setJdbcUrl(url);
    		config.setUsername(username);
    		config.setPassword(password);
    		
    		config.setMinConnectionsPerPartition(5);
    		config.setMaxConnectionsPerPartition(10);
    		config.setPartitionCount(2);
    		
    		cnxPool = new BoneCP(config);
    	}catch(SQLException e) {
    		e.printStackTrace();
    		throw new DAOConfigException("Error configuration connection pool",e);
    	}
    	
    	DAOFactory instance = new DAOFactory(cnxPool);
    	
    	return instance;
    }
    
    Connection getConnection() throws SQLException{
    	return cnxPool.getConnection();
    }
    
    public ClientDAO getClientDAO() {
    	return new ClientDAOImpl(this);
    }

    public OrderDAO getOrderDAO() {
		return new OrderDAOImpl(this);
	}
}
