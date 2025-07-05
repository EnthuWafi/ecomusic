<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/channel.css">
<div class="container mt-4">
	<!-- User Profile Section -->
	<div class="profile-header">
		<div class="profile-content">
			<div class="row align-items-center">
				<div class="col-md-2">
					<img
						src="${pageContext.request.contextPath}/stream/image/user/${artist.userId}"
						alt="${artist.username}" class="avatar-large">
				</div>
				<div class="col-md-10">
					<h2 class="mb-1">
						<c:out value="${artist.username}" />
					</h2>
					<h3 class="mb-2">
						<c:out value="${artist.firstName}" />
						<c:out value="${artist.lastName}" />
					</h3>

					<div class="profile-stats">
						<div class="stat-item">
							<span class="stat-number">${musicCount}</span> <span
								class="stat-label">Musics</span>
						</div>
						<div class="stat-item">
							<span class="stat-number">${playsCount}</span> <span
								class="stat-label">Times Music Played</span>
						</div>
						<div class="stat-item">
							<span class="stat-number">${playlistCount}</span> <span
								class="stat-label">Playlists</span>
						</div>
					</div>
					<div class="mt-3">
						<a href="mailto:<c:out value='${artist.email}'/>"
							class="btn btn-action"><i class="bi bi-envelope"></i></a>

					</div>
				</div>
			</div>
		</div>
	</div>

	<!-- Profile Navigation Tabs -->
	<div class="section-tabs">
		<ul class="nav nav-tabs" id="profileTabs" role="tablist">
			<li class="nav-item" role="presentation"><a class="nav-link"
				type="button" role="tab"
				href="${pageContext.request.contextPath}/channel/${artist.userId}">All</a></li>
			<li class="nav-item" role="presentation"><a class="nav-link"
				id="tracks-tab" type="button" role="tab"
				href="${pageContext.request.contextPath}/channel/${artist.userId}/music">Tracks</a></li>
			<li class="nav-item" role="presentation"><a class="nav-link"
				id="playlists-tab" type="button" role="tab"
				href="${pageContext.request.contextPath}/channel/${artist.userId}/playlist">Playlists</a></li>
		</ul>
	</div>

	<!-- Tab Content -->
	<div class="tab-content" id="profileTabContent">
		<div class="tab-pane fade show active" id="all" role="tabpanel">
			<div class="row">
				<div class="col-md-12">
					<div class="d-flex justify-content-between align-items-center mb-3">
						<h5 class="mb-3">Recent</h5>
						<a
							href="${pageContext.request.contextPath}/channel/${artist.userId}/music"
							class="text-muted small">View all</a>

					</div>

					<c:choose>
						<c:when test="${not empty musicList}">
							<c:forEach items="${musicList}" var="music">
								<div class="track-item"
									onclick="window.location.href = '${pageContext.request.contextPath}/music/play/${music.musicId}'">
									<button class="btn btn-dark btn-sm rounded-circle me-3">
										<i class="bi bi-play-fill"></i>
									</button>
									<img
										src="${pageContext.request.contextPath}/stream/image/music/${music.musicId}"
										alt="Track" class="track-thumbnail">
									<div class="track-details">
										<div class="track-name">
											<c:out value="${music.title}" />
										</div>
										<div class="track-meta">
											<fmt:formatDate value="${music.uploadDateDate}" type="date" />
										</div>
									</div>
								</div>
							</c:forEach>
						</c:when>
						<c:otherwise>
							<p>No music available.</p>
						</c:otherwise>
					</c:choose>

				</div>

				<div class="col-md-12">
					<div class="d-flex justify-content-between align-items-center mb-3">
						<h5 class="mb-3">Recent</h5>
						<a
							href="${pageContext.request.contextPath}/channel/${artist.userId}/playlist"
							class="text-muted small">View all</a>

					</div>

					<c:choose>
						<c:when test="${not empty playlistList}">
							<c:forEach items="${playlistList}" var="playlist">
								<div class="track-item"
									onclick="window.location.href = '${pageContext.request.contextPath}/playlist/play/${playlist.playlistId}'">

									<button class="btn btn-dark btn-sm rounded-circle me-3">
										<i class="bi bi-play-fill"></i>
									</button>

									<img
										src="${pageContext.request.contextPath}/stream/image/music/${playlist.musicList[0].music.musicId}?size=thumb"
										alt="Playlist Thumbnail" class="track-thumbnail">

									<div class="track-details">
										<div class="track-name">
											<c:out value="${playlist.name}" />
										</div>
										<div class="track-meta">
											<fmt:formatDate value="${playlist.createdAtDate}" type="date" />
											|
											<c:out value="${playlist.visibility}" />
										</div>
									</div>
								</div>
							</c:forEach>
						</c:when>

						<c:otherwise>
							<p>No playlists available.</p>
						</c:otherwise>
					</c:choose>

				</div>
			</div>
		</div>
	</div>
</div>