<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:if test="${not empty flashMessages}">
    <c:forEach var="msg" items="${flashMessages}">
        <script>
            toastr["${msg.type}"]("${msg.message}");
        </script>
    </c:forEach>
</c:if>
<!DOCTYPE html>
<html>
<head>
	<link rel="stylesheet" href="https://cdn.simplecss.org/simple.css">
    <title>${applicationScope.websiteName} - ${pageTitle}</title>
</head>
<body>

    <header>
        <h1>${applicationScope.websiteName}</h1>
        <nav>
            <a href="${pageContext.request.contextPath}/">Home</a> |
            <a href="${pageContext.request.contextPath}/music">Music Library</a> |
            <a href="${pageContext.request.contextPath}/login">Login</a> 
            <a href="${pageContext.request.contextPath}/register">Register</a> 
        </nav>
    </header>

    <main>
        <jsp:include page="${contentPage}" />
    </main>

    <footer>
        <p>&copy; 2025 Wafi Inc.</p>
    </footer>

</body>
</html>
