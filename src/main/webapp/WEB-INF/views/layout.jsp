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

<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.13.1/font/bootstrap-icons.min.css">
<meta charset="UTF-8">

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/custom.css">
<meta charset="UTF-8">

<!-- SCRIPTS -->
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"
	integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM"
	crossorigin="anonymous"></script>
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
						<c:when test="${sessionScope.user.roleName == 'admin'}">
							<a href="${pageContext.request.contextPath}/admin">Dashboard</a> |
           				</c:when>
						<c:when test="${sessionScope.user.roleName == 'artist'}">
							<a href="${pageContext.request.contextPath}/artist/music">Panel</a> |
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

			<form class="d-flex" role="search"
				action="${pageContext.request.contextPath}/music/search">
				<input class="form-control me-2 rounded-pill cute-search-input"
					type="search" placeholder="Find something lovely..."
					aria-label="Search" name="q">
				<button class="btn rounded-pill cute-search-btn" type="submit">
					<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16"
						fill="currentColor" class="bi bi-heart-fill" viewBox="0 0 16 16"
						style="vertical-align: -0.125em;">
                            <path fill-rule="evenodd"
							d="M8 1.314C12.438-3.248 23.534 4.735 8 15-7.534 4.736 3.562-3.248 8 1.314z" />
                        </svg>
				</button>
			</form>
		</nav>
	</header>

	<main>
		<c:import url="${contentPage}" />
	</main>

	<footer>
		<p>&copy; 2025 Wafi Inc.</p>
	</footer>

	<c:if test="${not empty flashMessages}">
		<script>
		
			<c:forEach var="msg" items="${flashMessages}">

			toastr["${msg.type}"]("${msg.message}");

			</c:forEach>
			
		</script>
	</c:if>

</body>
</html>
