<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<h2>All Music</h2>

<div class="container mt-4">
	<div class="row row-cols-1 row-cols-md-3 g-4">
		<c:forEach var="musicDTO" items="${musicList}">
			<div class="col">
				<div class="card h-100 shadow-sm">
					<img
						src="${pageContext.request.contextPath}/stream/image/music/${musicDTO.music.musicId}"
						class="card-img-top" alt="${musicDTO.music.title}"
						style="height: 200px; object-fit: cover;">
					<div class="card-body">
						<h5 class="card-title">${musicDTO.music.title}</h5>
						<h6 class="card-subtitle mb-2 text-muted"><a>${musicDTO.artistUsername}</a></h6>
						<h6 class="card-subtitle mb-2 text-muted">${musicDTO.music.genreName} - ${musicDTO.music.moodName}</h6>
						<p class="card-text">
			
							<small class="text-muted">Uploaded on:
								<fmt:parseDate value="${musicDTO.music.uploadDate}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime" type="both" />
								<fmt:formatDate pattern="dd.MM.yyyy HH:mm" value="${parsedDateTime}" />	 
							</small>

						</p>
						<div class="text-muted">
							<i class="bi bi-hand-thumbs-up me-1"></i> ${musicDTO.music.likeCount} <span
								class="mx-2">|</span> <i class="bi bi-eye me-1"></i>
							${musicDTO.music.totalPlayCount}
						</div>
						<p class="card-text">
							<span
								class="badge ${musicDTO.music.premiumContent ? 'bg-warning text-dark' : 'bg-secondary'}">
								${musicDTO.music.premiumContent ? 'Premium' : 'Free'} </span>
						</p>
					</div>
					<div class="card-footer text-center">
						<a
							href="${pageContext.request.contextPath}/music/play/${musicDTO.music.musicId}"
							class="btn btn-primary">Play</a>
					</div>
				</div>
			</div>
		</c:forEach>
	</div>
</div>


<!-- Pagination controls -->
<div>
	<c:if test="${currentPage > 1}">
		<a href="?page=${currentPage - 1}">Previous</a>
	</c:if>

	<c:forEach var="i" begin="1" end="${totalPages}">
		<a href="?page=${i}">${i}</a>
	</c:forEach>

	<c:if test="${currentPage < totalPages}">
		<a href="?page=${currentPage + 1}">Next</a>
	</c:if>
</div>