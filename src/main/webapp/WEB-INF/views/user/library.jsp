<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<div class="library-container">
    <div class="library-header">
        <h1>Your Music Library</h1>
        <div class="library-actions">
            <a href="${pageContext.request.contextPath}/user/playlist/create" class="btn btn-primary">
                <i class="fas fa-plus"></i> Create Playlist
            </a>
        </div>
    </div>

    <div class="library-content">
        <!-- Quick Access Section -->
        <div class="quick-access">
            <div class="quick-access-item">
                <div onclick="window.location.href = '${pageContext.request.contextPath}/user/liked'" class="quick-link">
                    <div class="quick-icon liked-songs">
                        <i class="fas fa-heart"></i>
                    </div>
                    <div class="quick-info">
                        <h3>Liked Songs</h3>
                        <p>${fn:length(likes)} songs</p>
                    </div>
                </div>
            </div>
        </div>

        <!-- Recently Liked Songs -->
        <c:if test="${not empty likes}">
            <div class="library-section">
                <div class="section-header">
                    <h2>Recently Liked</h2>
                    <a href="${pageContext.request.contextPath}/user/liked" class="view-all-link">View All</a>
                </div>
                <div class="liked-tracks">
                    <c:forEach var="like" items="${likes}" varStatus="status">
                        <c:if test="${status.index < 5}"> <!-- Show only 5 recent liked songs -->
                            <div class="track-item" onclick="playTrack(${like.music.musicId})">
                                <img src="${like.music.imageUrl}" alt="${like.music.title}" class="track-image">
                                <div class="track-info">
                                    <span class="track-title">${like.music.title}</span>
                                    <span class="track-artist">${like.music.artist}</span>
                                </div>
                                <div class="track-actions">
                                    <button class="play-btn-small" onclick="event.stopPropagation(); playTrack(${like.music.musicId})">
                                        <i class="fas fa-play"></i>
                                    </button>
                                    <span class="liked-date">
                                    <fmt:parseDate value="${like.likedAt}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime" type="both" />
								<fmt:formatDate pattern="dd.MM.yyyy HH:mm" value="${parsedDateTime}" />	 
								</span>
                                </div>
                            </div>
                        </c:if>
                    </c:forEach>
                </div>
            </div>
        </c:if>

        <!-- Playlists Section -->
        <div class="library-section">
            <div class="section-header">
                <h2>Recently Created</h2>
                <c:if test="${not empty playlists}">
                    <a href="${pageContext.request.contextPath}/user/playlists" class="view-all-link">View All</a>
                </c:if>
            </div>

            <c:choose>
                <c:when test="${empty playlists}">
                    <div class="empty-library">
                        <div class="empty-icon">
                            <i class="fas fa-music fa-3x"></i>
                        </div>
                        <h3>No playlists yet</h3>
                        <p>Start building your music collection by creating your first playlist!</p>
                        <a href="${pageContext.request.contextPath}/user/playlist/create" class="btn btn-primary">
                            Create Your First Playlist
                        </a>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="playlist-grid">
                        <c:forEach var="playlist" items="${playlists}" varStatus="status">
                            <c:if test="${status.index < 6}"> <!-- Show only first 6 playlists -->
                                <div class="playlist-card">
                                    <div onclick="window.location.href = '${pageContext.request.contextPath}/user/playlist/${playlist.playlistId}'" class="playlist-link">
                                        <div class="playlist-image">
                                            <c:choose>
                                                <c:when test="${not empty playlist.musicList and not empty playlist.musicList[0].music.imageUrl}">
                                                    <img src="${pageContext.request.contextPath}/stream/image/music/${playlist.musicList[0].music.musicId}" alt="${playlist.name}" />
                                                </c:when>
                                                <c:otherwise>
                                                    <div class="placeholder-image">
                                                        <i class="fas fa-music"></i>
                                                    </div>
                                                </c:otherwise>
                                            </c:choose>
                                            <div class="playlist-overlay">
                                                <div class="play-btn">
                                                    <i class="fas fa-play"></i>
                                                </div>
                                            </div>
                                        </div>
                                        
                                        <div class="playlist-info">
                                            <h3 class="playlist-name">${playlist.name}</h3>
                                            <p class="playlist-meta">
                                                <c:choose>
                                                    <c:when test="${empty playlist.musicList}">
                                                        0 songs
                                                    </c:when>
                                                    <c:when test="${fn:length(playlist.musicList) == 1}">
                                                        1 song
                                                    </c:when>
                                                    <c:otherwise>
                                                        ${fn:length(playlist.musicList)} songs
                                                    </c:otherwise>
                                                </c:choose>
                                                • ${playlist.visibility}
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

        <!-- Recently Played Section (if you want to add this later) -->
        <c:if test="${not empty recentlyPlayed}">
            <div class="library-section">
                <div class="section-header">
                    <h2>Recently Played</h2>
                </div>
                <div class="recent-tracks">
                    <c:forEach var="track" items="${recentlyPlayed}" varStatus="status">
                        <c:if test="${status.index < 5}"> <!-- Show only 5 recent tracks -->
                            <div class="track-item">
                                <img src="${track.imageUrl}" alt="${track.title}" class="track-image">
                                <div class="track-info">
                                    <span class="track-title">${track.title}</span>
                                    <span class="track-artist">${track.artist}</span>
                                </div>
                            </div>
                        </c:if>
                    </c:forEach>
                </div>
            </div>
        </c:if>
    </div>
</div>
