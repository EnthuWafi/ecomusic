<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<div class="container mt-4">
	<div class="d-flex justify-content-between align-items-center mb-3">
		<h2 class="mb-0">Search Result</h2>

	</div>

	<!-- (2) List of tracks -->
	<div class="list-group">
		<c:forEach var="musicDTO" items="${musicList}">
			<div
				class="list-group-item list-group-item-action d-flex align-items-center py-3">
				<!-- Left: cover image -->
				<div class="me-3">
					<a
						href="${pageContext.request.contextPath}/music/play/${musicDTO.music.musicId}">
						<img
						src="${pageContext.request.contextPath}/stream/image/music/${musicDTO.music.musicId}"
						class="rounded"
						style="width: 80px; height: 80px; object-fit: cover;"
						alt="${musicDTO.music.title} cover" />
					</a>
				</div>

				<!-- Center: title + genre/mood + likes/views -->
				<div class="flex-fill">
					<!-- Title + Premium badge -->
					<div class="d-flex justify-content-between align-items-start">
						<h5 class="mb-1">
							<a
								href="${pageContext.request.contextPath}/music/play/${musicDTO.music.musicId}"
								class="text-decoration-none text-body">
								${musicDTO.music.title} </a>
						</h5>
						<c:if test="${musicDTO.music.premiumContent}">
							<span class="badge bg-warning text-dark">Premium</span>
						</c:if>
					</div>

					<!-- Genre • Mood -->
					<small class="text-muted d-block mb-1">
						${musicDTO.music.genreName} • ${musicDTO.music.moodName} </small>

					<!-- Likes & Views -->
					<div class="text-muted">
						<i class="bi bi-hand-thumbs-up me-1"></i> ${musicDTO.music.likeCount} <span
							class="mx-2">|</span> <i class="bi bi-eye me-1"></i>
						${musicDTO.music.totalPlayCount}
					</div>
				</div>

			</div>
		</c:forEach>

		<!-- If no tracks found -->
		<c:if test="${empty musicList}">
			<div class="list-group-item text-center text-muted py-4">
				Unfortunately, no music found!</div>
		</c:if>
	</div>

	<!-- (3) Pagination controls -->
	<nav aria-label="Music pagination" class="mt-4">
		<ul class="pagination justify-content-center">
			<c:if test="${currentPage > 1}">
				<li class="page-item"><a class="page-link"
					href="?page=${currentPage - 1}">Previous</a></li>
			</c:if>

			<c:forEach var="i" begin="1" end="${totalPages}">
				<li class="page-item ${i == currentPage ? 'active' : ''}"><a
					class="page-link" href="?page=${i}">${i}</a></li>
			</c:forEach>

			<c:if test="${currentPage < totalPages}">
				<li class="page-item"><a class="page-link"
					href="?page=${currentPage + 1}">Next</a></li>
			</c:if>
		</ul>
	</nav>
</div>