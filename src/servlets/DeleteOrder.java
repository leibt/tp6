package servlets;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.Order;
import dao.DAOFactory;
import dao.OrderDAO;
import exceptions.DAOException;

/**
 * Servlet implementation class DeleteOrder
 */
@WebServlet("/delete-order")
public class DeleteOrder extends HttpServlet {
	

	private static final String VIEW_LIST_ORDER = "/orders-list";
	private static final String ATT_LIST_COM 	= "ordersList";
	
	public static final String ATT_DAO_FACTORY	= "daoFactory";
	   
	private OrderDAO orderDAO;
    
    public void init() throws ServletException{
    	this.orderDAO = ( (DAOFactory) getServletContext().getAttribute(ATT_DAO_FACTORY) ).getOrderDAO();
    }
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String idOrder = request.getParameter("id");
		Long id = Long.parseLong(idOrder);
		
		HttpSession session = request.getSession();
		
		Map<Long,Order> ordersList = (Map<Long,Order>)session.getAttribute(ATT_LIST_COM);
		
		if(ordersList != null) {	
			try {
				orderDAO.delete(ordersList.get(id));
				ordersList.remove(id);
				session.setAttribute(ATT_LIST_COM, ordersList);
			}catch(DAOException e) {
				e.printStackTrace();
			}
		}

		response.sendRedirect(request.getContextPath()+VIEW_LIST_ORDER);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
