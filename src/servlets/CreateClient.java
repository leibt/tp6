package servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.Client;
import dao.ClientDAO;
import dao.DAOFactory;
import forms.ClientForm;

/**
 * Servlet implementation class CreateClient
 */
@WebServlet(urlPatterns="/create-client", initParams = @WebInitParam(name="path", value="/Users/leibenlo/Documents/"))
@MultipartConfig( location = "C:/Users/leibenlo/Documents", maxFileSize = 1048576, maxRequestSize = 5242880, fileSizeThreshold = 1048576 )
public class CreateClient extends HttpServlet {
 
	private static final String ATT_CLIENT      = "client";
	private static final String ATT_FORM	    = "form";
	private static final String ATT_LIST_CLI	= "clientsList";
	private static final String ATT_PATH		= "path";
	private static final String ATT_DAO_FACTORY = "daoFactory";
 
    public static final String VIEW_SUCCES    	= "/WEB-INF/showClient.jsp";
    public static final String VIEW_FORM    	= "/WEB-INF/createClient.jsp";
	
    private ClientDAO clientDAO;
    
    public void init() throws ServletException{
    	this.clientDAO = ( (DAOFactory) getServletContext().getAttribute(ATT_DAO_FACTORY) ).getClientDAO();
    }
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		this.getServletContext().getRequestDispatcher(VIEW_FORM).forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String path = this.getServletConfig().getInitParameter(ATT_PATH);
		ClientForm fc = new ClientForm(clientDAO);
		Client client = fc.creaCli(request,path);
		
		HttpSession session = request.getSession();
				
        request.setAttribute( ATT_CLIENT, client );
        request.setAttribute( ATT_FORM, fc );
        
        if(fc.getErrors().isEmpty()) {
        	Map<Long, Client> clientList = (HashMap<Long, Client>) session.getAttribute(ATT_LIST_CLI); 
        	
        	if(clientList == null)
        		clientList = new HashMap<>();
        	
        	clientList.put(client.getId(),client);
        	session.setAttribute("clientList", clientList);
        	this.getServletContext().getRequestDispatcher(VIEW_SUCCES).forward(request, response);
        }else {
        	this.getServletContext().getRequestDispatcher(VIEW_FORM).forward(request, response);
        }
        	
	}

}
