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
                <a href="${pageContext.request.contextPath}/user/liked" class="quick-link">
                    <div class="quick-icon liked-songs">
                        <i class="fas fa-heart"></i>
                    </div>
                    <div class="quick-info">
                        <h3>Liked Songs</h3>
                        <p>${fn:length(likes)} songs</p>
                    </div>
                </a>
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
                                    <a href="${pageContext.request.contextPath}/user/playlist/${playlist.playlistId}" class="playlist-link">
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
                                    </a>
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

<script>
function playTrack(musicId) {
    // Implement track play functionality
    // This could redirect to a player or use AJAX to start playing
    console.log('Playing track:', musicId);
    // Example: window.location.href = '${pageContext.request.contextPath}/play/' + musicId;
}
</script>

<style>
.quick-access {
    margin-bottom: 2rem;
}

.quick-access-item {
    display: inline-block;
    margin-right: 1rem;
}

.quick-link {
    display: flex;
    align-items: center;
    padding: 1rem;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    border-radius: 8px;
    color: white;
    text-decoration: none;
    transition: transform 0.2s;
}

.quick-link:hover {
    transform: translateY(-2px);
    color: white;
}

.quick-icon {
    width: 50px;
    height: 50px;
    border-radius: 4px;
    display: flex;
    align-items: center;
    justify-content: center;
    margin-right: 1rem;
    font-size: 1.5rem;
}

.liked-songs {
    background: linear-gradient(135deg, #ff6b6b, #ee5a24);
}

.library-section {
    margin-bottom: 2rem;
}

.section-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 1rem;
}

.section-header h2 {
    margin: 0;
    font-size: 1.5rem;
    font-weight: 600;
}

.view-all-link {
    color: #667eea;
    text-decoration: none;
    font-weight: 500;
}

.view-all-link:hover {
    text-decoration: underline;
}

.playlist-card {
    transition: transform 0.2s;
}

.playlist-card:hover {
    transform: translateY(-4px);
}

.playlist-link {
    text-decoration: none;
    color: inherit;
}

.liked-tracks {
    display: flex;
    flex-direction: column;
    gap: 0.5rem;
}

.track-item {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 0.75rem;
    border-radius: 8px;
    transition: background-color 0.2s;
    cursor: pointer;
}

.track-item:hover {
    background-color: #f8f9fa;
}

.track-item > div:first-child {
    display: flex;
    align-items: center;
    flex: 1;
}

.track-image {
    width: 50px;
    height: 50px;
    border-radius: 6px;
    margin-right: 1rem;
    object-fit: cover;
}

.track-info {
    display: flex;
    flex-direction: column;
    flex: 1;
}

.track-title {
    font-weight: 500;
    font-size: 0.95rem;
    margin-bottom: 0.25rem;
}

.track-artist {
    color: #666;
    font-size: 0.85rem;
}

.track-actions {
    display: flex;
    align-items: center;
    gap: 1rem;
}

.play-btn-small {
    width: 35px;
    height: 35px;
    border-radius: 50%;
    border: none;
    background: #667eea;
    color: white;
    display: flex;
    align-items: center;
    justify-content: center;
    cursor: pointer;
    transition: all 0.2s;
    opacity: 0;
}

.track-item:hover .play-btn-small {
    opacity: 1;
}

.play-btn-small:hover {
    background: #5a67d8;
    transform: scale(1.05);
}

.liked-date {
    color: #888;
    font-size: 0.8rem;
    min-width: 120px;
    text-align: right;
}
</style>