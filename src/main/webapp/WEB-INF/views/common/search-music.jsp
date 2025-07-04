<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>


<div class="container mt-4">

	<form id="filter-search" method="get" class="mb-4">

		<c:if test="${not empty param.q}">
			<input type="hidden" name="q" value="${param.q}">
		</c:if>
		<div class="row">
			<div class="col-md-6">
				<div class="card shadow-sm">
					<div class="card-header fw-bold">Genre</div>
					<div class="card-body">
						<div class="row row-cols-2 ms-2">
							<c:forEach var="genre" items="${genreList}">
								<div class="form-check">
									<input class="form-check-input genre-filter" type="checkbox"
										name="genre" value="${genre.genreId}"
										id="genre${genre.genreId}" /> <label class="form-check-label"
										for="genre${genre.genreId}"> ${genre.name} </label>
								</div>
							</c:forEach>
						</div>
					</div>
				</div>
			</div>

			<div class="col-md-6">
				<div class="card shadow-sm">
					<div class="card-header fw-bold">Mood</div>
					<div class="card-body">
						<div class="row row-cols-2 ms-2">
							<c:forEach var="mood" items="${moodList}">

								<div class="form-check">
									<input class="form-check-input mood-filter" type="checkbox"
										name="mood" value="${mood.moodId}" id="mood${mood.moodId}" />
									<label class="form-check-label" for="mood${mood.moodId}">
										${mood.name} </label>
								</div>
							</c:forEach>
						</div>
					</div>
				</div>
			</div>
		</div>

		<!-- Filter Button -->
		<div class="text-end mt-3">
			<button type="submit" class="btn btn-primary">
				<i class="bi bi-funnel me-1"></i> Filter
			</button>
		</div>
	</form>



	<div class="d-flex justify-content-between align-items-center mb-3">
		<h2 class="mb-0">Search Result</h2>
	</div>

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

				<div class="flex-fill">
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

					<small class="text-muted d-block mb-1">
						${musicDTO.music.genreName} • ${musicDTO.music.moodName} </small>

					<div class="text-muted">
						<i class="bi bi-hand-thumbs-up me-1"></i>
						${musicDTO.music.likeCount} <span class="mx-2">|</span> <i
							class="bi bi-eye me-1"></i> ${musicDTO.music.totalPlayCount}
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

	<!-- Pagination controls -->
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
<script>
document.addEventListener('DOMContentLoaded', () => {
	
  	const params = new URLSearchParams(window.location.search);

	const applyCheckedFromParams = (name) => {
	  const values = params.getAll(name);
	  document.querySelectorAll(`input[name="\${name}"]`).forEach(input => {
	    input.checked = values.includes(input.value.toString());
	  });
	};
	
	applyCheckedFromParams("genre");
	applyCheckedFromParams("mood");

    window.latestSearchTerm = '';

    window.addEventListener('globalSearchTerm', e => {
        window.latestSearchTerm = e.detail;
      });
    
    const form = document.querySelector('#filter-search');
    if (form) {
		form.addEventListener('submit', (e) => {
		
	 	 if (window.latestSearchTerm && window.latestSearchTerm.trim().length > 0) {
	 		const existing = form.querySelector('input[name="q"]');
	     	if (existing) {
		       existing.remove();
		     }
		  	
		    const qInput = document.createElement('input');
		    qInput.type = 'hidden';
		    qInput.name = 'q';
		    qInput.value = window.latestSearchTerm.trim();
		    form.appendChild(qInput);
	 	 }
	});
  }

    
});
</script>
