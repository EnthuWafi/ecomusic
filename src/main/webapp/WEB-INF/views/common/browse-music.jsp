<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<h2>All Music</h2>

<div class="container mt-4">
	<div class="row row-cols-1 row-cols-sm-2 row-cols-md-4 g-4">
		<c:forEach var="musicDTO" items="${musicList}">
			<div class="col">
				<div class="card h-100 bg-dark text-light border-0 shadow-sm">


					<img
						src="${pageContext.request.contextPath}/stream/image/music/${musicDTO.music.musicId}?size=thumb"
						class="card-img-top object-fit-cover"
						style="height: 180px; width: 100%;" alt="${musicDTO.music.title}">


					<div class="card-body">
						<h5
							class="card-title mb-1 text-truncate text-white text-decoration-none"
							title="${musicDTO.music.title}">
							<a
								href="${pageContext.request.contextPath}/music/play/${musicDTO.music.musicId}">${musicDTO.music.title}</a>
						</h5>
						<p class="card-text mb-2 small text-secondary">by
							${musicDTO.artistUsername}</p>
						<div class="d-flex align-items-center flex-wrap gap-2">

							<!-- Free / Premium badge -->
							<span
								class="badge rounded-pill 
				${musicDTO.music.premiumContent ? 'bg-warning text-dark' : 'bg-primary'}">
								${musicDTO.music.premiumContent ? 'Premium' : 'Free'} </span>

							<!-- Play count -->
							<span class="small text-muted d-flex align-items-center">
								<i class="bi bi-play-fill me-1"></i>
								${musicDTO.music.totalPlayCountString}
							</span>

							<!-- Like count -->
							<span class="small text-muted d-flex align-items-center">
								<i class="bi bi-heart-fill me-1"></i>
								${musicDTO.music.likeCountString}
							</span>

						</div>
					</div>

				</div>
			</div>
		</c:forEach>
	</div>
</div>


<!-- Pagination controls -->
<nav>
	<ul class="pagination justify-content-center pt-4 text-dark">

		<c:if test="${currentPage > 1}">
			<li class="page-item"><a class="page-link"
				href="?page=${currentPage - 1}">Previous</a></li>
		</c:if>

		<c:forEach var="i" begin="1" end="${totalPages}">
			<li class="page-item"><a class="page-link" href="?page=${i}">${i}</a></li>
		</c:forEach>

		<c:if test="${currentPage < totalPages}">
			<li class="page-item"><a class="page-link"
				href="?page=${currentPage + 1}">Next</a></li>
		</c:if>

	</ul>
</nav>