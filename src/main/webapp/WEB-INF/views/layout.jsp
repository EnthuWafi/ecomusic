<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>

<!-- CSS -->
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css"
	rel="stylesheet"
	integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC"
	crossorigin="anonymous">
<link rel="stylesheet" href="https://cdn.simplecss.org/simple.css">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/toastr.js/2.1.4/toastr.css"
	integrity="sha512-oe8OpYjBaDWPt2VmSFR+qYOdnTjeV9QPLJUeqZyprDEQvQLJ9C5PCFclxwNuvb/GQgQngdCXzKSFltuHD3eCxA=="
	crossorigin="anonymous" referrerpolicy="no-referrer" />


<!-- SCRIPTS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.7.1/jquery.min.js"
	integrity="sha512-v2CJ7UaYy4JwqLDIrZUI/4hqeoQieOmAZNXBeQyjo21dadnwR+8ZaIJVT8EE2iyI61OV8e6M8PP2/4hpQINQ/g=="
	crossorigin="anonymous" referrerpolicy="no-referrer"></script>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/toastr.js/2.1.4/toastr.min.js"
	integrity="sha512-lbwH47l/tPXJYG9AcFNoJaTMhGvYWhVM9YI43CT+uteTRRaiLCui8snIgyAN8XWgNjNhCqlAUdzZptso6OCoFQ=="
	crossorigin="anonymous" referrerpolicy="no-referrer"></script>

<title>${applicationScope.websiteName}-${pageTitle}</title>
</head>
<body>

	<header>
		<h1>${applicationScope.websiteName}</h1>
		<nav>
			<a href="${pageContext.request.contextPath}/">Home</a> | <a
				href="${pageContext.request.contextPath}/music">Browse Music</a> |

			<c:choose>
				<c:when test="${not empty sessionScope.user}">
				
                <span>Welcome, ${sessionScope.user.username}!</span>
                
                <c:choose>
						<c:when test="${sessionScope.user.userType == 'admin'}">
							<a href="${pageContext.request.contextPath}/admin">Dashboard</a> |
           			</c:when>
						<c:when test="${sessionScope.user.userType == 'artist'}">
							<a href="${pageContext.request.contextPath}/artist">Panel</a> |
           			</c:when>
					</c:choose>

					<a href="${pageContext.request.contextPath}/profile">Profile</a> |
                <a href="${pageContext.request.contextPath}/logout">Logout</a>
				</c:when>
				<c:otherwise>
					<a href="${pageContext.request.contextPath}/login">Login</a> |
                <a href="${pageContext.request.contextPath}/register">Register</a>
				</c:otherwise>
			</c:choose>

		</nav>
	</header>

	<main>
		<jsp:include page="${contentPage}" />
	</main>

	<footer>
		<p>&copy; 2025 Wafi Inc.</p>
	</footer>

	<c:if test="${not empty flashMessages}">
		<c:forEach var="msg" items="${flashMessages}">
			<script>
				toastr["${msg.type}"]("${msg.message}");
			</script>
		</c:forEach>
	</c:if>

</body>
</html>
