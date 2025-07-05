<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>


<div class="container py-4">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h1>Your Music Library</h1>
        <a href="${pageContext.request.contextPath}/user/playlist/create" class="btn btn-primary">
            <i class="bi bi-plus-circle me-1"></i> Create Playlist
        </a>
    </div>

    <div>
        <c:choose>
            <c:when test="${empty playlists}">
                <div class="text-center p-5 border rounded">
                    <i class="bi bi-music-note-beamed display-4 mb-3"></i>
                    <h3>No playlists yet</h3>
                    <p>Start building your music collection by creating your first playlist!</p>
                    <a href="${pageContext.request.contextPath}/user/playlist/create" class="btn btn-primary">
                        <i class="bi bi-plus-circle"></i> Create Your First Playlist
                    </a>
                </div>
            </c:when>
            <c:otherwise>
                <div class="row row-cols-1 row-cols-sm-2 row-cols-md-3 g-4">
                    <c:forEach var="playlist" items="${playlists}" varStatus="status">
                        <c:if test="${status.index < 6}">
                            <div class="col">
                                <div class="card h-100 shadow-sm">
                                    <a href="${pageContext.request.contextPath}/user/playlist/${playlist.playlistId}" class="text-decoration-none text-dark">
                                        <c:choose>
                                            <c:when test="${not empty playlist.musicList and not empty playlist.musicList[0].music}">
                                                <img src="${pageContext.request.contextPath}/stream/image/music/${playlist.musicList[0].music.musicId}?size=thumb" 
                                                     class="card-img-top" alt="${playlist.name}" />
                                            </c:when>
                                            <c:otherwise>
                                                <div class="d-flex align-items-center justify-content-center bg-secondary bg-opacity-25" style="height: 200px;">
                                                    <i class="bi bi-music-note-beamed fs-1"></i>
                                                </div>
                                            </c:otherwise>
                                        </c:choose>

                                        <div class="card-body">
                                            <h5 class="card-title">${playlist.name}</h5>
                                            <p class="card-text text-muted mb-2">
                                                <c:choose>
                                                    <c:when test="${empty playlist.musicList}">0 songs</c:when>
                                                    <c:when test="${playlist.playlistCount == 1}">1 song</c:when>
                                                    <c:otherwise>${playlist.playlistCount} songs</c:otherwise>
                                                </c:choose>
                                                • ${playlist.visibility}
                                            </p>
                                        </div>
                                    </a>
                                    <div class="card-footer border-0 d-flex justify-content-end">
                                        <a href="${pageContext.request.contextPath}/user/playlist/${playlist.playlistId}/edit" class="btn btn-outline-secondary btn-sm">
                                            <i class="bi bi-pencil"></i> Edit
                                        </a>
                                        <button type="button"
											class="ms-2 btn btn-outline-danger btn-sm btn-delete-playlist"
											data-playlist-id="${playlist.playlistId}">
											<i class="bi bi-trash-fill me-1"></i> Delete
										</button>
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

<script>
  $(function () {
    $('.btn-delete-playlist').click(function () {
      const playlistId = $(this).data('playlist-id');
      const deleteUrl = '${pageContext.request.contextPath}/api/playlist/' + playlistId;

      if (confirm('Delete this playlist?')) {
        $.ajax({
          url: deleteUrl,
          type: 'DELETE',
          success: function () {
            alert('Playlist deleted successfully.');
            location.reload();
          },
          error: function (xhr, status, error) {
            alert('Failed to delete playlist. Please try again.');
            console.error('Delete failed:', error);
          }
        });
      }
    });
  });
</script>
