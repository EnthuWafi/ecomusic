<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<div class="container my-5">
  <!-- Header -->
  <div class="d-flex justify-content-between align-items-center mb-4">
    <h1 class="h3">Your Music Library</h1>
    <a href="${pageContext.request.contextPath}/user/playlist/create" class="btn btn-primary">
      <i class="bi bi-plus-lg me-1"></i>Create Playlist
    </a>
  </div>

  <!-- Quick Access -->
  <div class="row mb-5">
    <div class="col-sm-6 col-md-4 col-lg-3">
      <div class="card text-white bg-danger h-100" role="button"
           onclick="location.href='${pageContext.request.contextPath}/user/liked'">
        <div class="card-body d-flex align-items-center">
          <i class="bi bi-heart-fill display-4 me-3"></i>
          <div>
            <h5 class="card-title mb-1">Liked Songs</h5>
            <p class="card-text mb-0">${likeCount} songs</p>
          </div>
        </div>
      </div>
    </div>
  </div>

  <!-- Recently Liked -->
  <c:if test="${not empty likes}">
    <div class="mb-5">
      <div class="d-flex justify-content-between align-items-baseline mb-3">
        <h2 class="h5 mb-0">Recently Liked</h2>
        <a href="${pageContext.request.contextPath}/user/liked" class="small">View All</a>
      </div>
      <ul class="list-group">
        <c:forEach var="like" items="${likes}" varStatus="st">
          <c:if test="${st.index < 5}">
            <li class="list-group-item  list-group-item-action d-flex align-items-center pe-auto" onclick="location.href='${pageContext.request.contextPath}/music/play/${like.music.musicId}'">
              <img src="${pageContext.request.contextPath}/stream/image/music/${like.music.musicId}?size=thumb" alt="${like.music.title}"
                   class="rounded me-3" style="width:48px; height:48px; object-fit:cover;">
              <div class="flex-grow-1">
                <div class="fw-semibold">${like.music.title}</div>
              </div>
              <small class="text-muted">
                <fmt:formatDate value="${like.likedAtDate}" type="date"/>
              </small>
            </li>
          </c:if>
        </c:forEach>
      </ul>
    </div>
  </c:if>

  <!-- Recently Created Playlists -->
  <div class="mb-5">
    <div class="d-flex justify-content-between align-items-baseline mb-3">
      <h2 class="h5 mb-0">Recently Created</h2>
      <c:if test="${not empty playlists}">
        <a href="${pageContext.request.contextPath}/user/playlists" class="small">View All</a>
      </c:if>
    </div>

    <c:choose>
      <c:when test="${empty playlists}">
        <div class="text-center text-secondary py-5">
          <i class="bi bi-music-note-list display-4 mb-3"></i>
          <h3 class="h6 mb-2">No playlists yet</h3>
          <p>Create your first playlist to get started.</p>
          <a href="${pageContext.request.contextPath}/user/playlist/create" class="btn btn-primary">
            <i class="bi bi-plus-lg me-1"></i>Create Playlist
          </a>
        </div>
      </c:when>
      <c:otherwise>
        <div class="row g-4">
          <c:forEach var="pl" items="${playlists}" varStatus="st">
            <c:if test="${st.index < 6}">
              <div class="col-6 col-md-4 col-lg-3">
                <div class="card h-100 shadow-sm pe-auto" onclick="location.href='${pageContext.request.contextPath}/user/playlist/${pl.playlistId}'">
                  <div class="position-relative">
                    <c:choose>
                      <c:when test="${not empty pl.musicList and not empty pl.musicList[0].music.imageUrl}">
                        <img src="${pageContext.request.contextPath}/stream/image/music/${pl.musicList[0].music.musicId}?size=thumb"
                             class="card-img-top" alt="${pl.name}" style="height:160px; object-fit:cover;">
                      </c:when>
                      <c:otherwise>
                        <div class="bg-secondary bg-opacity-10 d-flex align-items-center justify-content-center"
                             style="height:160px;">
                          <i class="bi bi-music-note-list display-4 text-secondary"></i>
                        </div>
                      </c:otherwise>
                    </c:choose>
                  </div>
                  <div class="card-body">
                    <h5 class="card-title text-truncate mb-1">${pl.name}</h5>
                    <p class="card-text small text-secondary mb-0">
                      ${pl.playlistCount} 
                      ${pl.playlistCount == 1 ? 'song' : 'songs'} • ${pl.visibility}
                    </p>
                  </div>
                </div>
              </div>
            </c:if>
          </c:forEach>
        </div>
      </c:otherwise>
    </c:choose>
  </div>
</div>
