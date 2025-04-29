<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<h1>User Details</h1>

<c:if test="${not empty user}">
	<table border="1">
		<tr>
			<th>User ID</th>
			<td>${user.userId}</td>
		</tr>
		<tr>
			<th>Name</th>
			<td>${user.name}</td>
		</tr>
		<tr>
			<th>Email</th>
			<td>${user.email}</td>
		</tr>
		<tr>
			<th>Password</th>
			<td>${user.password}</td>
		</tr>
		<tr>
			<th>User Type</th>
			<td>${user.userType}</td>
		</tr>
		<tr>
			<th>Created At</th>
			<td><fmt:formatDate value="${user.createdAt}" pattern="yyyy-MM-dd" /></td>
		</tr>
	</table>
</c:if>

<c:if test="${empty user}">
	<p>User information is not available.</p>
</c:if>

<a href="${pageContext.request.contextPath}">Back to Home</a>