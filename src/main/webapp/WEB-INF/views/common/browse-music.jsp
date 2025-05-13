<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<h2>All Music</h2>

<div class="container mt-4">
	<div class="row row-cols-1 row-cols-md-3 g-4">
		<c:forEach var="music" items="${musicList}">
			<div class="col">
				<div class="card h-100 shadow-sm">
					<img src="${pageContext.request.contextPath}/stream/image/music/${music.musicId}" class="card-img-top" 
					alt="${music.title}" style="height: 200px; object-fit: cover;">
					<div class="card-body">
						<h5 class="card-title">${music.title}</h5>
						<h6 class="card-subtitle mb-2 text-muted">${music.genre}</h6>
						<p class="card-text">
							<small class="text-muted">Uploaded on: 
								<fmt:formatDate value="${music.uploadDate}" pattern="yyyy-MM-dd" />
							</small>
						</p>
						<p class="card-text">
							<span class="badge ${music.premiumContent ? 'bg-warning text-dark' : 'bg-secondary'}">
								${music.premiumContent ? 'Premium' : 'Free'}
							</span>
						</p>
					</div>
					<div class="card-footer text-center">
						<a href="${pageContext.request.contextPath}/music/play/${music.musicId}" class="btn btn-primary">Play</a>
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