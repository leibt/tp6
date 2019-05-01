package forms;

import static forms.FormUtilities.getFieldValue;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.joda.time.DateTime;

import beans.Client;
import beans.Order;
import dao.ClientDAO;
import dao.OrderDAO;
import exceptions.FormValidationException;

public class OrderForm {

    public static final String DATE_FIELD             	= "dateOrder";
    public static final String TOTAL_FIELD          	= "totalOrder";
    public static final String PAYMENT_METHOD_FIELD  	= "paymentMethodOrder";
    public static final String PAYMENT_STATUS_FIELD 	= "paymentStatusOrder";
    public static final String DELIVERY_METHOD_FIELD	= "deliveryMethodOrder";
    public static final String DELIVERY_STATUS_FIELD 	= "deliveryStatusOrder";
    public static final String CLIENT_CHOICE_FIELD	  	= "choiceNewClient";
    public static final String CLIENT_NAME_EXI_FIELD	= "existingClient";
    
    public static final String FORMAT_DATE            	= "dd/MM/yyyy HH:mm:ss";
    public static final String ATT_LIST_CLI			  	= "clientsList";
    
    private Map<String,String> errors;
	private String msg;
    
	private ClientDAO clientDAO;
	private OrderDAO orderDAO;
	
	public OrderForm(ClientDAO clientDAO, OrderDAO orderDAO) {
		this.clientDAO = clientDAO;
		this.orderDAO = orderDAO;
	}
	
	public OrderForm(OrderDAO orderDAO) {
		this.orderDAO = orderDAO;
	}
    
    public Order creaCom (HttpServletRequest request, String path) {
    	
    	String paymentMethod 	= getFieldValue(request, PAYMENT_METHOD_FIELD );
        String paymentStatus 	= getFieldValue(request, PAYMENT_STATUS_FIELD );
        String deliveryMethod 	= getFieldValue(request, DELIVERY_METHOD_FIELD );
        String deliveryStatus 	= getFieldValue(request, DELIVERY_STATUS_FIELD );
        
        String total		 	= getFieldValue(request, TOTAL_FIELD );
    	
        String isnewClient 		= getFieldValue(request, CLIENT_CHOICE_FIELD);
        String label_newClient 	= "newClient";
       
        Client cli;
        
        if( "oldClient".equals(isnewClient) ) {
			String clientName = getFieldValue(request, CLIENT_NAME_EXI_FIELD);
        	
    		HttpSession session = request.getSession();
			Map<Long,Client> cliList = (HashMap<Long,Client>)session.getAttribute(ATT_LIST_CLI);
			
			cli = cliList.get(clientName);
			errors = new HashMap<>();
    	}else {
    		ClientForm fc = new ClientForm(clientDAO);
    	    cli = fc.creaCli(request,path);
    	    errors=fc.getErrors();
    	}
        
        Order order = new Order();
        order.setClient(cli);
    	
    	
        DateTime date = new DateTime();
        order.setDate(date);
        //org.joda.time.format.DateTimeFormatter formatter = DateTimeFormat.forPattern( FORMAT_DATE );
        //String date = dt.toString( formatter );
        
        
        
        checkTotal(total,order);
        checkMethod(PAYMENT_METHOD_FIELD, paymentMethod, order);
        checkStatus(PAYMENT_STATUS_FIELD, paymentStatus, order);
        checkMethod(DELIVERY_METHOD_FIELD, deliveryMethod, order);
        checkStatus(DELIVERY_STATUS_FIELD, deliveryStatus, order);      
        
        if(errors.isEmpty()) {
        	msg="Succès de la création de la commande";
        }else {
        	msg="Echec de la création de la commande";
        }
    	
    	return order;
    }
    
    private void checkTotal(String total_temp,Order order) {
        double total = 0;
        
        try {
        	if(total_temp != null) {		
        		try {
        			total = Double.parseDouble(total_temp);
            		if(total < 0) 
            			throw new FormValidationException("Please enter a valid total");
           
                } catch ( NumberFormatException e ) {
                	total= -1;
                	throw new FormValidationException("Please enter a valid total");
                }
   
        	}else {
        		throw new FormValidationException("Please enter a total");
        	}        	
        }catch(FormValidationException e) {
        	setError(TOTAL_FIELD, e.getMessage());
        }
        order.setTotal(total);
    }
        
    private void checkMethod(String label, String methodValue, Order order) {
    	try {
    		if(methodValue != null) {
        		if(methodValue.trim().length() < 2)
        			throw new FormValidationException("This field must contains at least 2 characters");
        	}else {
        		throw new FormValidationException("Please enter a valid method");
        	}
        }catch(FormValidationException e) {
        	switch(label) {
        		case PAYMENT_METHOD_FIELD:
        			setError(PAYMENT_METHOD_FIELD,e.getMessage());
        		break;
        		case DELIVERY_METHOD_FIELD:
        			setError(DELIVERY_METHOD_FIELD,e.getMessage());
        		break;	
        	}	
        }
    	
    	switch(label) {
			case PAYMENT_METHOD_FIELD:
				order.setPaymentMethod(methodValue);
			break;
			case DELIVERY_METHOD_FIELD:
				order.setDeliveryMethod(methodValue);
			break;	
		}		
    }
    
    private void checkStatus(String label, String statusValue, Order order) {
    	 try {
    		if(statusValue != null) {
    	    	if(statusValue.trim().length() < 2) {
    	       		throw new FormValidationException("This field must contains at least 2 characters");
    	    	}
    	    }
         }catch(FormValidationException e) {
        	 switch(label) {
	 			case PAYMENT_METHOD_FIELD:
	 				setError(PAYMENT_STATUS_FIELD,e.getMessage());
	 			break;
	 			case DELIVERY_METHOD_FIELD:
	 				setError(DELIVERY_STATUS_FIELD,e.getMessage());
	 			break;	
	 		}          	
         }
    	 
    	 switch(label) {
			case PAYMENT_METHOD_FIELD:
				order.setPaymentStatus(statusValue);
			break;
			case DELIVERY_METHOD_FIELD:
				order.setDeliveryStatus(statusValue);
			break;	
		}
    }
    
    
    private void setError(String champ, String valeur) {
		errors.put(champ, valeur);
	}

	public String getMsg() {
		return msg;
	}

	public Map<String, String> getErrors() {
		return errors;
	}
		
}
