<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="app-base-url" content="${pageContext.request.contextPath}">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>${applicationScope.websiteName}-${pageTitle}</title>
<!-- Bootstrap CSS -->
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/bootstrap.min.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/toastr.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/bootstrap-icons.min.css">

<script
	src="${pageContext.request.contextPath}/assets/js/react.development.js"></script>
<script
	src="${pageContext.request.contextPath}/assets/js/react-dom.development.js"></script>
<script
	src="${pageContext.request.contextPath}/assets/js/react-bootstrap.min.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/babel.min.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/toastr.min.js"></script>
<script
	src="${pageContext.request.contextPath}/assets/js/bootstrap.bundle.min.js"></script>

<meta charset="UTF-8">
<!-- Custom CSS -->
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body class="text-light">

	<!-- Top Navbar -->
	<nav class="navbar navbar-expand-lg navbar-dark bg-dark px-3">
		<a class="navbar-brand d-flex align-items-center" href="#"> <img
			src="${pageContext.request.contextPath}/assets/images/logo.svg"
			alt="Logo" width="24" height="24" /> <span class="fs-4 fw-bold">${applicationScope.websiteName}</span>
		</a>
		<button class="navbar-toggler" type="button" data-bs-toggle="collapse"
			data-bs-target="#navbarNav" aria-controls="navbarNav"
			aria-expanded="false" aria-label="Toggle navigation">
			<span class="navbar-toggler-icon"></span>
		</button>
		<div class="collapse navbar-collapse justify-content-between"
			id="navbarNav">
			
			<form action="${pageContext.request.contextPath}/music/search"
				method="get">
			
			<ul class="navbar-nav">
				<li class="nav-item px-2"><a class="nav-link active"
					aria-current="page" href="${pageContext.request.contextPath}/home">Home</a></li>
				<li class="nav-item px-2"><a class="nav-link"
					href="${pageContext.request.contextPath}/music/search">Browse</a></li>
				<li class="nav-item px-2"><a class="nav-link"
					href="${pageContext.request.contextPath}/user/library">Library</a>
				</li>
				<li class="nav-item px-2 w-100" id="search-bar-root">
				</li>
			</ul>
			
			</form>

			<ul class="navbar-nav ms-auto">
				<c:choose>
					<c:when test="${not empty sessionScope.user}">
						<li class="nav-item dropdown"><a
							class="nav-link dropdown-toggle" href="#" id="userDropdown"
							role="button" data-bs-toggle="dropdown" aria-expanded="false">
								<img
								src="${pageContext.request.contextPath}/stream/image/user/${sessionScope.user.userId}"
								alt="Profile" width="24" height="24" class="rounded-circle me-1">
								${sessionScope.user.username}
						</a>
							<ul class="dropdown-menu dropdown-menu-end"
								aria-labelledby="userDropdown">
								<li><a class="dropdown-item"
									href="${pageContext.request.contextPath}/user/profile">Profile</a></li>
								<li><hr class="dropdown-divider"></li>
								<li><a class="dropdown-item"
									href="${pageContext.request.contextPath}/logout">Logout</a></li>
							</ul></li>
					</c:when>
					<c:otherwise>
						<li class="nav-item"><a class="nav-link"
							href="${pageContext.request.contextPath}/login">Login</a></li>
						<li class="nav-item"><a class="nav-link"
							href="${pageContext.request.contextPath}/register">Register</a></li>
					</c:otherwise>
				</c:choose>
			</ul>
		</div>
	</nav>

	<div class="container-fluid">
		<div class="row">
			<!-- Sidebar -->
			<nav class="col-md-2 d-none d-lg-block bg-dark sidebar px-3 pt-4"
				id="sidebar">
				<h6 class="text-uppercase text-muted">Your Library</h6>
				<ul class="nav flex-column mb-3">
					<li class="nav-item"><a class="nav-link text-white px-0"
						href="#">Recently Played</a></li>
					<li class="nav-item"><a class="nav-link text-white px-0"
						href="#">Liked Songs</a></li>
					<li class="nav-item"><a class="nav-link text-white px-0"
						href="#">Favourite Artists</a></li>
				</ul>
				<h6 class="text-uppercase text-muted">Playlists</h6>
				<ul class="nav flex-column">
					<li class="nav-item"><a class="nav-link text-white px-0"
						href="#">Workout Mix</a></li>
					<li class="nav-item"><a class="nav-link text-white px-0"
						href="#">Chill Vibes</a></li>
					<li class="nav-item"><a class="nav-link text-white px-0"
						href="#">Party Hits</a></li>
					<li class="nav-item"><a class="nav-link text-white px-0"
						href="#">Road Trip</a></li>
				</ul>
			</nav>

			<!-- Main Content -->
			<main class="col-md-10 px-4 pt-4" id="main-content">
				<c:import url="${contentPage}" />
			</main>
		</div>
	</div>

	<script
		src="${pageContext.request.contextPath}/assets/js/components/SearchBar.js"></script>

	<script>
		const baseUrl = document.querySelector('meta[name="app-base-url"]')
				.getAttribute('content');

		const container = document.getElementById('search-bar-root');
		const root = ReactDOM.createRoot(container);
		root.render(React.createElement(SearchBar, {
			baseUrl : baseUrl
		}));
	</script>

</body>
</html>
