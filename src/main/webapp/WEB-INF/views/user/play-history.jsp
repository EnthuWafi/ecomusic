<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<div class="container my-5">
	<h2 class="mb-4">Your Play History</h2>
	<p>You have wasted <span class="fw-bold"><c:out value="${listeningTime}"/></span> listening to music on this app!</p>
	<c:choose>
		<c:when test="${not empty playHistoryList}">
			<div class="list-group">
				<c:forEach var="history" items="${playHistoryList}">
					<div onclick="window.location.href = '${pageContext.request.contextPath}/music/play/${history.music.musicId}'"
						class="list-group-item list-group-item-action d-flex align-items-center">
						<img
							src="${pageContext.request.contextPath}/stream/image/music/${history.music.musicId}?size=thumb"
							alt="cover" class="rounded me-3"
							style="width: 80px; height: 80px; object-fit: cover;">
						<div class="flex-grow-1">
							<h5 class="mb-1">${history.music.title}</h5>
							<p class="mb-1 text-muted">${history.music.genreName} - ${history.music.moodName}</p>
							<small> <fmt:formatDate value="${history.playedAtDate}"
									type="date" /></small><br> 
							<small>Last listened for
								${history.listenDurationString} <c:if
									test="${history.wasSkipped}">(Skipped)</c:if>
							</small>
						</div>
					</div>
				</c:forEach>
			</div>
		</c:when>
		<c:otherwise>
			<div class="alert alert-info">No play history available.</div>
		</c:otherwise>
	</c:choose>
</div>