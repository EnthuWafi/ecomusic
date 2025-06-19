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
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bootstrap.min.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/toastr.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bootstrap-icons.min.css">

<script src="${pageContext.request.contextPath}/assets/js/react.development.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/react-dom.development.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/react-bootstrap.min.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/babel.min.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/toastr.min.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/bootstrap.bundle.min.js"></script>

<meta charset="UTF-8">
<!-- Custom CSS -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body class="text-light">

	<!-- Top Navbar -->
	<nav class="navbar navbar-expand-lg navbar-dark bg-dark px-3">
		<a class="navbar-brand d-flex align-items-center" href="#">
			<img src="${pageContext.request.contextPath}/assets/images/logo.svg" alt="Logo" width="24" height="24" />
			<span class="fs-4 fw-bold">${applicationScope.websiteName}</span>
		</a>
		<button class="navbar-toggler" type="button" data-bs-toggle="collapse"
			data-bs-target="#navbarNav" aria-controls="navbarNav"
			aria-expanded="false" aria-label="Toggle navigation">
			<span class="navbar-toggler-icon"></span>
		</button>
		<div class="collapse navbar-collapse justify-content-between"
			id="navbarNav">
			<ul class="navbar-nav">
				<li class="nav-item px-2"><a class="nav-link active"
					aria-current="page" href="#">Home</a></li>
				<li class="nav-item px-2"><a class="nav-link" href="#">Browse</a>
				</li>
				<li class="nav-item px-2"><a class="nav-link" href="#">Library</a>
				</li>
			</ul>
			<form action="${pageContext.request.contextPath}/music/search" method="get">
			<div id="search-bar-root" class="w-100 pt-1"></div>
			</form>
		</div>
	</nav>

	<div class="container-fluid">
		<div class="row">
			<!-- Sidebar -->
			<nav class="col-md-2 d-none d-md-block bg-dark sidebar px-3 pt-4" id="sidebar">
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
	
	<script src="${pageContext.request.contextPath}/assets/js/components/SearchBar.js"></script>
	
	<script>
	  const baseUrl = document.querySelector('meta[name="app-base-url"]').getAttribute('content');

	  const container = document.getElementById('search-bar-root');
	  const root = ReactDOM.createRoot(container);
	  root.render(React.createElement(SearchBar, { baseUrl: baseUrl }));
	</script>

</body>
</html>
