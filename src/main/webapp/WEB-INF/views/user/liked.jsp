<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<div class="container my-5">
	<h2 class="mb-4">Liked Songs</h2>

	<c:choose>
		<c:when test="${not empty likeList}">
			<div class="list-group">
				<c:forEach var="like" items="${likeList}">
					<div class="list-group-item list-group-item-action d-flex align-items-center" onclick="window.location.href= '${pageContext.request.contextPath}/music/play/${like.music.musicId}'">
						<img
						src="${pageContext.request.contextPath}/stream/image/music/${like.music.musicId}?size=thumb"
						alt="cover" class="rounded me-3"
						style="width: 64px; height: 64px; object-fit: cover;">

						<div class="flex-grow-1">
							<h5 class="mb-1">${like.music.title}</h5>
							<small class="text-muted"> ${like.music.genreName} –
								${like.music.moodName} <br/> 
								Liked on <fmt:formatDate
									value="${like.likedAtDate}" pattern="MMM d, yyyy" />
							</small>
						</div>
						<div class="d-flex gap-3">
							<small class="text-muted d-flex align-items-center"> <i
								class="bi bi-play-fill me-1"></i>
								${like.music.totalPlayCountString}
							</small> <small class="text-muted d-flex align-items-center"> <i
								class="bi bi-heart-fill me-1"></i> ${like.music.likeCountString}
							</small>
						</div>
					</div>
				</c:forEach>
			</div>
		</c:when>
		<c:otherwise>
			<div class="alert alert-info">You haven't liked any songs yet.</div>
		</c:otherwise>
	</c:choose>
</div>