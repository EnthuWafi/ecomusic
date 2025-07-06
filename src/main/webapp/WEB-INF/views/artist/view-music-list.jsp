<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<div class="container mt-4">
	<div class="d-flex justify-content-between align-items-center mb-3">
		<h2 class="mb-0">My Music Studio</h2>
		<a href="${pageContext.request.contextPath}/artist/music/upload"
			class="btn btn-primary"> <i class="bi bi-plus-circle me-1"></i>
			Add New Track
		</a>
	</div>

	<div class="list-group">
		<c:forEach var="music" items="${musicList}">
			<div
				class="list-group-item list-group-item-action d-flex align-items-center py-3">
				<div class="me-3">
					<img
						src="${pageContext.request.contextPath}/stream/image/music/${music.musicId}?size=thumb"
						class="rounded"
						style="width: 80px; height: 80px; object-fit: cover;" />
				</div>

				<div class="flex-fill">
					<div class="d-flex justify-content-between align-items-start">
						<h5 class="mb-1"
						onclick="location.href = '${pageContext.request.contextPath}/music/play/${music.musicId}'"
						style="cursor: pointer;"
						>${music.title}</h5>
					</div>

					<small class="text-muted d-block mb-1 text-dark">
						${music.genreName} • ${music.moodName} <c:if
							test="${music.premiumContent}"> 
              • <span class="badge bg-warning text-dark">Premium</span>
						</c:if>
					</small>

					<div class="text-muted">
						<i class="bi bi-hand-thumbs-up me-1"></i> ${music.likeCountString}
						<span class="mx-2">|</span> <i class="bi bi-eye me-1"></i>
						${music.totalPlayCountString}
					</div>
				</div>

				<!-- Right: Edit button -->
				<div>
					<a
						href="${pageContext.request.contextPath}/artist/music/edit/${music.musicId}"
						class="btn btn-outline-secondary btn-sm"> <i
						class="bi bi-pencil-square me-1"></i> Edit
					</a>
					<button type="button"
						class="me-1 btn btn-outline-danger btn-sm btn-delete-music"
						data-music-id="${music.musicId}">
						<i class="bi bi-trash-fill me-1"></i> Delete
					</button>


				</div>
			</div>
		</c:forEach>

		<!-- If no tracks found -->
		<c:if test="${empty musicList}">
			<div class="list-group-item text-center text-muted py-4">You
				haven’t uploaded any tracks yet.</div>
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

<script>
	$(function() {
		$('.btn-delete-music')
		.click(
			function() {
				const musicId = $(this).data('music-id');
				const deleteUrl = '${pageContext.request.contextPath}/api/music/' + musicId;

				if (confirm('Delete this music?')) {
					$
					.ajax({
						url : deleteUrl,
						type : 'DELETE',
						success : function() {
							alert('Music deleted successfully.');
							location.reload();
						},
						error : function(xhr, status, error) {
							alert('Failed to delete music. Please try again.');
							console.error('Delete failed:',
									error);
						}
					});
				}
			});
	});
</script>
