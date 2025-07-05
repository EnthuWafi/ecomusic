<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>


<div class="container my-5">
	<div class="mb-4 text-center">
		<h1>Edit Playlist</h1>
		<p class="text-muted">Modify your playlist settings</p>
	</div>

	<div class="card shadow-sm">
		<div class="card-body">
			<form id="editPlaylistForm"
				action="${pageContext.request.contextPath}/user/playlist/${playlist.playlistId}/edit"
				method="post">

				<!-- Playlist Name -->
				<div class="mb-3">
					<label for="playlistName" class="form-label"> <i
						class="bi bi-music-note-list me-1"></i>Playlist Name *
					</label> <input type="text" id="playlistName" name="name"
						class="form-control" placeholder="Enter playlist name..."
						value="${playlist.name}" required maxlength="100">
					<div class="form-text">Give your playlist a memorable name.</div>
				</div>

				<!-- Visibility -->
				<fieldset class="mb-4">
					<legend class="form-label">
						<i class="bi bi-eye-fill me-1"></i>Privacy Settings
					</legend>
					<div class="d-flex gap-3">
						<div class="form-check">
							<input class="form-check-input" type="radio" name="visibility"
								id="visibilityPublic" value="public"
								${playlist.visibility.value == 'public' ? 'checked' : ''}>
							<label class="form-check-label" for="visibilityPublic"> <i
								class="bi bi-globe2 me-1"></i> <strong>Public</strong> <small
								class="d-block text-muted">Anyone can find and listen</small>
							</label>
						</div>
						<div class="form-check">
							<input class="form-check-input" type="radio" name="visibility"
								id="visibilityPrivate" value="private"
								${playlist.visibility.value == 'private' ? 'checked' : ''}>
							<label class="form-check-label" for="visibilityPrivate">
								<i class="bi bi-lock-fill me-1"></i> <strong>Private</strong> <small
								class="d-block text-muted">Only you can access</small>
							</label>
						</div>
					</div>
				</fieldset>

				<!-- Current Playlist Info -->
				<div class="mb-4 p-3 rounded">
					<h5>
						<i class="bi bi-info-circle me-1"></i>Playlist Information
					</h5>
					<div class="row text-secondary">
						<div class="col-sm-8 mb-2">
							<small>Created:</small><br> <strong><fmt:formatDate
									value="${playlist.createdAtDate}" type="date" /></strong>
						</div>
						<div class="col-sm-4 mb-2">
							<small>Visibility:</small><br> <strong> <i
								class="bi ${playlist.visibility.value == 'public' ? 'bi-globe2' : 'bi-lock-fill'} me-1"></i>
								${playlist.visibility.value}
							</strong>
						</div>
					</div>
				</div>

				<div class="d-flex justify-content-between">
					<a
						onclick="window.history.back()"
						class="btn btn-outline-secondary"> <i
						class="bi bi-arrow-left me-1"></i>Cancel
					</a>

					<div>
						<button type="button"
							class="btn btn-danger me-2 btn-delete-playlist"
							data-playlist-id="${playlist.playlistId}">
							<i class="bi bi-trash-fill me-1"></i> Delete
						</button>

						<button type="submit" class="btn btn-primary" id="saveBtn">
							<i class="bi bi-save me-1"></i>Save Changes
						</button>
					</div>
				</div>
			</form>
		</div>
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

