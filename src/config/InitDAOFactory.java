package config;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import dao.DAOFactory;

@WebListener
public class InitDAOFactory implements ServletContextListener{

	private DAOFactory daoFactory;
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ServletContext context = sce.getServletContext();
		this.daoFactory = DAOFactory.getInstance();
		context.setAttribute("daoFactory", this.daoFactory);
	}

}
