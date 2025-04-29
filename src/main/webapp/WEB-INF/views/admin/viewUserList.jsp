<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<h2>All Users</h2>
<a class="button" href="${pageContext.request.contextPath}/user/add">Add
	New User</a>


<table border="1">
    <thead>
        <tr>
            <th>User ID</th>
            <th>Name</th>
            <th>Email</th>
            <th>User Type</th>
            <th>Created At</th>
            <th>Actions</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach var="user" items="${userList}">
            <tr>
                <td><a href="${pageContext.request.contextPath}/user/${user.userId}">${user.userId}</a></td>
                <td>${user.name}</td>
                <td>${user.email}</td>
                <td>${user.userType}</td>
                <td><fmt:formatDate value="${user.createdAt}" pattern="yyyy-MM-dd" /></td>
                <td>
                    <a href="${pageContext.request.contextPath}/user/edit/${user.userId}">Edit</a> |
                    <a onclick="return confirm('Are you sure you want to delete this user?');">Delete</a>
                </td>
            </tr>
        </c:forEach>
    </tbody>
</table>