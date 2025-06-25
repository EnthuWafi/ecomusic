<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en" data-bs-theme="dark">
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
<script src="${pageContext.request.contextPath}/assets/js/chart.js"></script>

<script
	src="${pageContext.request.contextPath}/assets/js/bootstrap.bundle.min.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/toastr.min.js"></script>

<script src="https://unpkg.com/wavesurfer.js@7.9.7/dist/wavesurfer.min.js"></script>

<meta charset="UTF-8">
<!-- Custom CSS -->
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/style.css">

</head>
<body>

	<script>
	  window.baseUrl = document.querySelector('meta[name="app-base-url"]').getAttribute('content');
	</script>
	<c:set var="user" value="${sessionScope.user}" />
	<!-- Top Navbar -->
	<nav class="navbar navbar-expand-lg navbar-dark bg-dark px-3">
		<a class="navbar-brand d-flex align-items-center"
			href="${pageContext.request.contextPath}/home"> <img
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
			<ul class="navbar-nav me-auto">
				<li class="nav-item px-2"><a class="nav-link"
					href="${pageContext.request.contextPath}/home">Home</a></li>
				<li class="nav-item px-2"><a class="nav-link"
					href="${pageContext.request.contextPath}/music/browse">Browse</a></li>

				<c:if test="${not empty user && (user.user or user.premium)}">
					<li class="nav-item px-2"><a class="nav-link"
						href="${pageContext.request.contextPath}/user/library">Library</a>
					</li>
				</c:if>

				<c:if test="${not empty user && user.artist}">
					<li class="nav-item px-2"><a class="nav-link"
						href="${pageContext.request.contextPath}/user/library">Library</a>
					</li>
					<li class="nav-item px-2"><a class="nav-link"
						href="${pageContext.request.contextPath}/artist/music">Music</a></li>
				</c:if>

				<c:if test="${not empty user && (user.admin || user.superAdmin)}">
					<li class="nav-item px-2"><a class="nav-link"
						href="${pageContext.request.contextPath}/admin/dashboard">Dashboard</a>
					</li>

					<li class="nav-item px-2 dropdown"><a
						class="nav-link dropdown-toggle" href="#"
						data-bs-toggle="dropdown">Admin Tools</a>
						<ul class="dropdown-menu dropdown-menu-dark">
							<li><a class="dropdown-item"
								href="${pageContext.request.contextPath}/admin/user">Users</a></li>
							<li><a class="dropdown-item"
								href="${pageContext.request.contextPath}/admin/music">Music</a></li>
							<li><a class="dropdown-item"
								href="${pageContext.request.contextPath}/admin/subscription">Subscriptions</a></li>
							<li><a class="dropdown-item"
								href="${pageContext.request.contextPath}/admin/reports">Reports</a></li>
						</ul></li>

				</c:if>
			</ul>

			<!-- CENTER SEARCH BAR -->
			<div class="d-lg-block" id="search-bar-root"></div>

			<!-- RIGHT CTA / USER -->
			<ul class="navbar-nav ms-auto">
				<c:choose>
					<c:when test="${not empty user}">
						<!-- User Dropdown -->
						<li class="nav-item px-2 dropdown"><a
							class="nav-link dropdown-toggle d-flex align-items-center"
							href="#" id="userDropdown" role="button"
							data-bs-toggle="dropdown" aria-expanded="false"> <img
								src="${pageContext.request.contextPath}/stream/image/user/${user.userId}"
								alt="Profile" width="24" height="24" class="rounded-circle me-1">
								${user.username}
						</a>
							<ul class="dropdown-menu dropdown-menu-dark dropdown-menu-end"
								aria-labelledby="userDropdown">
								<c:if test="${!user.admin}">
									<li><a class="dropdown-item"
										href="${pageContext.request.contextPath}/user/profile">Profile</a>
									</li>
								</c:if>

								<c:if test="${!(user.admin or user.superAdmin)}">
									<li><a class="dropdown-item"
										href="${pageContext.request.contextPath}/become-artist">Become
											an Artist</a></li>
									<li><a class="dropdown-item"
										href="${pageContext.request.contextPath}/choose-plan">Go
											Premium</a></li>
								</c:if>

								<li><hr class="dropdown-divider" /></li>
								<li><a class="dropdown-item"
									href="${pageContext.request.contextPath}/logout">Logout</a></li>
							</ul></li>
					</c:when>

					<c:otherwise>
						<li class="nav-item px-2"><a class="btn btn-outline-light"
							href="${pageContext.request.contextPath}/become-artist">
								Become an Artist </a></li>
						<li class="nav-item px-2"><a
							class="btn btn-warning text-dark"
							href="${pageContext.request.contextPath}/choose-plan"> Go
								Premium </a></li>
						<li class="nav-item px-2"><a class="nav-link"
							href="${pageContext.request.contextPath}/login">Login</a></li>
						<li class="nav-item px-2"><a class="nav-link"
							href="${pageContext.request.contextPath}/register">Register</a></li>
					</c:otherwise>
				</c:choose>
			</ul>
		</div>
	</nav>

	<div class="container-fluid">
		<div class="row">
			<!-- Sidebar -->
			<c:if
				test="${not empty user and not (user.admin or user.superAdmin)}">
				<nav class="col-md-2 d-none d-lg-block bg-dark sidebar px-3 pt-4">
					<h6 class="text-uppercase">Your Library</h6>
					<ul class="nav flex-column mb-3">
						<li class="nav-item"><a class="nav-link text-white px-0"
							href="#">Play History</a></li>
						<li class="nav-item"><a class="nav-link text-white px-0"
							href="#">Liked Songs</a></li>
					</ul>
					<div id="playlist-sidebar-root"></div>
				</nav>
			</c:if>
			<c:if test="${empty user}">
				<nav class="col-md-2 d-none d-lg-block bg-dark sidebar px-3 pt-4">

				</nav>
			</c:if>
			<c:if test="${not empty user and (user.admin or user.superAdmin)}">
				<nav class="col-md-2 d-none d-lg-block bg-dark sidebar px-3 pt-4">
					<h6 class="text-uppercase">Manage</h6>
					<ul class="nav flex-column mb-3">
						<li class="nav-item"><a class="nav-link text-white"
							href="${pageContext.request.contextPath}/admin/dashboard"><i
								class="bi bi-speedometer"></i> Dashboard</a></li>
						<li class="nav-item"><a class="nav-link text-white"
							href="${pageContext.request.contextPath}/admin/user"><i
								class="bi bi-people"></i> Users</a></li>
						<li class="nav-item"><a class="nav-link text-white"
							href="${pageContext.request.contextPath}/admin/music"><i
								class="bi bi-apple-music"></i> Music</a></li>
						<li class="nav-item"><a class="nav-link text-white"
							href="${pageContext.request.contextPath}/admin/subscription"><i
								class="bi bi-calendar-check"></i> Subscriptions</a></li>
						<li class="nav-item"><a class="nav-link text-white"
							href="${pageContext.request.contextPath}/admin/reports"><i
								class="bi bi-graph-up-arrow"></i> Reports</a></li>
						<li class="nav-item"><a class="nav-link text-white"
							href="${pageContext.request.contextPath}/admin/logs"><i
								class="bi bi-clipboard-data"></i> Logs</a></li>
					</ul>
				</nav>
			</c:if>

			<!-- Main Content -->
			<main class="col-md-10 px-4 pt-4 text-white" id="main-content">
				<c:import url="${contentPage}" />
			</main>
		</div>
	</div>

	<c:if
		test="${empty user or (not empty user and not (user.admin or user.superAdmin))}">
		<script
			src="${pageContext.request.contextPath}/assets/js/components/SearchBar.js"></script>

		<script>	
			const searchContainer = document.getElementById('search-bar-root');
			const searchRoot = ReactDOM.createRoot(searchContainer);
			searchRoot.render(React.createElement(SearchBar, {
				baseUrl : window.baseUrl
			}));
		</script>
	</c:if>

	<c:if test="${not empty user and not (user.admin or user.superAdmin)}">

		<script
			src="${pageContext.request.contextPath}/assets/js/components/PlaylistSidebar.js"></script>

		<script>
		const userId = ${(not empty user) ? user.userId : 'null'};
	
		const containerPlaylist = document.getElementById('playlist-sidebar-root');
		const rootPlaylist = ReactDOM.createRoot(containerPlaylist);
		rootPlaylist.render(React.createElement(PlaylistSidebar, {
			baseUrl : window.baseUrl,
			userId : userId
		}));
	
	</script>
	</c:if>

	<c:if test="${not empty flashMessages}">
		<script>
		toastr.options.positionClass = 'toast-bottom-right';
		
		<c:forEach var="msg" items="${flashMessages}">

			toastr["${msg.type}"]("${msg.message}");

		</c:forEach>
		</script>
	</c:if>

	<script>
	$(document).ready(function () {
	    const path = window.location.pathname;

	    $('.nav-link').each(function () {
	        const href = $(this).attr('href');

	        if (path === href || path.startsWith(href)) {
	            $(this).addClass('active');
	        }
	    });
	});
	</script>

</body>
</html>
