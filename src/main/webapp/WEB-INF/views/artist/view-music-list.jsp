<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="container mt-4">
  <div class="d-flex justify-content-between align-items-center mb-3">
    <h2 class="mb-0">My Music Studio</h2>
    <a
      href="${pageContext.request.contextPath}/artist/music/upload"
      class="btn btn-primary"
    >
      <i class="bi bi-plus-circle me-1"></i> Add New Track
    </a>
  </div>

  <!-- (2) List of tracks -->
  <div class="list-group">
    <c:forEach var="music" items="${musicList}">
      <div
        class="list-group-item list-group-item-action d-flex align-items-center py-3"
      >
        <!-- Left: cover image -->
        <div class="me-3">
          <img
            src="${pageContext.request.contextPath}/stream/image/music/${music.musicId}?size=thumb"
            class="rounded"
            style="width: 80px; height: 80px; object-fit: cover;"
          />
        </div>

        <!-- Center: title + genre/mood + likes/views -->
        <div class="flex-fill">
          <!-- Title + Premium badge -->
          <div class="d-flex justify-content-between align-items-start">
            <h5 class="mb-1">${music.title}</h5>
            <c:if test="${music.premiumContent}">
              <span class="badge bg-warning text-dark">Premium</span>
            </c:if>
          </div>
          
          <!-- Genre • Mood -->
          <small class="text-muted d-block mb-1 text-dark">
            ${music.genreName} • ${music.moodName}
          </small>
          
          <!-- Likes & Views -->
          <div class="text-muted">
            <i class="bi bi-hand-thumbs-up me-1"></i> ${music.likeCountString}
            <span class="mx-2">|</span>
            <i class="bi bi-eye me-1"></i> ${music.totalPlayCountString}
          </div>
        </div>

        <!-- Right: Edit button -->
        <div>
          <a
            href="${pageContext.request.contextPath}/artist/music/edit/${music.musicId}"
            class="btn btn-outline-secondary btn-sm"
          >
            <i class="bi bi-pencil-square me-1"></i> Edit
          </a>
        </div>
      </div>
    </c:forEach>

    <!-- If no tracks found -->
    <c:if test="${empty musicList}">
      <div class="list-group-item text-center text-muted py-4">
        You haven’t uploaded any tracks yet.
      </div>
    </c:if>
  </div>

  <!-- (3) Pagination controls -->
  <nav aria-label="Music pagination" class="mt-4">
    <ul class="pagination justify-content-center">
      <c:if test="${currentPage > 1}">
        <li class="page-item">
          <a class="page-link" href="?page=${currentPage - 1}">Previous</a>
        </li>
      </c:if>

      <c:forEach var="i" begin="1" end="${totalPages}">
        <li class="page-item ${i == currentPage ? 'active' : ''}">
          <a class="page-link" href="?page=${i}">${i}</a>
        </li>
      </c:forEach>

      <c:if test="${currentPage < totalPages}">
        <li class="page-item">
          <a class="page-link" href="?page=${currentPage + 1}">Next</a>
        </li>
      </c:if>
    </ul>
  </nav>
</div>