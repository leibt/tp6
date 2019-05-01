<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Clients list</title>
	<link type="text/css" rel="stylesheet" href="inc/style.css" /> 
</head>
<body>

	<c:import url="/inc/menu.jsp"/>
	
	
	<c:choose>
		<c:when test="${empty clientsList }">
			<p class="erreur">No client save</p>
		</c:when>
		<c:otherwise>
			<table>
			  <tr>
			    <th>Name</th>
			    <th>Firstname</th>
			    <th>Address</th>
			    <th>Phone</th>
			    <th>Email</th>
			    <th>Image</th>
			    <th class="action">Action</th>
			  </tr>
			  
			  <c:forEach var="client" items="${sessionScope.clientsList }" varStatus="flag">	
			  	<tr>
			  		<td><c:out value="${ client.value.nom }" /></td>
			  		<td><c:out value="${ client.value.prenom }" /></td>
			  		<td><c:out value="${ client.value.adresse }" /></td>
			  		<td><c:out value="${ client.value.telephone }" /></td>
			  		<td> <c:out value="${ client.value.email }" /></td>
			  		<td>
			  			<c:if test="${not empty client.value.image }">
			  				<a target="_blank" href="<c:url value="/images/${client.value.image }" />">VOIR</a>
			  			</c:if>
			  		</td>
			  		<td class="action"><a href="<c:url value='/delete-client' ><c:param name='idClient' value='${client.value.id }' /></c:url>"><strong>X</strong></a></td>
			  	</tr>
			  </c:forEach>
			  
			</table>
		</c:otherwise>
	</c:choose>
	

</body>
</html>