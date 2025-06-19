<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div class="create-playlist-container">
	<div class="create-playlist-header">
		<h1>Create New Playlist</h1>
		<p>Build your perfect music collection</p>
	</div>

	<div class="create-playlist-content">
		<form id="createPlaylistForm"
			action="${pageContext.request.contextPath}/user/playlist/create"
			method="post" class="playlist-form">

			<!-- Playlist Name -->
			<div class="form-group">
				<label for="playlistName" class="form-label"> <i
					class="fas fa-music"></i> Playlist Name *
				</label> <input type="text" id="playlistName" name="name"
					class="form-control" placeholder="Enter playlist name..." required
					maxlength="100"> <small class="form-text">Give your
					playlist a memorable name</small>
			</div>

			<!-- Visibility -->
			<div class="form-group">
				<label class="form-label"> <i class="fas fa-eye"></i>
					Privacy Settings
				</label>
				<div class="visibility-options">
					<div class="radio-option">
						<input type="radio" id="visibilityPublic" name="visibility"
							value="public" checked> <label for="visibilityPublic"
							class="radio-label">
							<div class="radio-content">
								<i class="fas fa-globe"></i>
								<div>
									<strong>Public</strong>
									<p>Anyone can find and listen to this playlist</p>
								</div>
							</div>
						</label>
					</div>

					<div class="radio-option">
						<input type="radio" id="visibilityPrivate" name="visibility"
							value="private"> <label for="visibilityPrivate"
							class="radio-label">
							<div class="radio-content">
								<i class="fas fa-lock"></i>
								<div>
									<strong>Private</strong>
									<p>Only you can access this playlist</p>
								</div>
							</div>
						</label>
					</div>
				</div>
			</div>

			<!-- Form Actions -->
			<div class="form-actions">
				<button type="button" class="btn btn-secondary"
					onclick="window.history.back()">
					<i class="fas fa-arrow-left"></i> Cancel
				</button>

				<button type="submit" class="btn btn-primary" id="createBtn">
					<i class="fas fa-plus"></i> Create Playlist
				</button>
			</div>
		</form>

	</div>
</div>


<script>
document.getElementById('createPlaylistForm').addEventListener('submit', function(e) {
    const playlistName = document.getElementById('playlistName').value.trim();
    
    if (!playlistName) {
        e.preventDefault();
        alert('Please enter a playlist name');
        return;
    }
    
    if (playlistName.length < 1 || playlistName.length > 100) {
        e.preventDefault();
        alert('Playlist name must be between 1 and 100 characters');
        return;
    }
    
    // Disable submit button to prevent double submission
    const createBtn = document.getElementById('createBtn');
    createBtn.disabled = true;
    createBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Creating...';
});


// Auto-focus on playlist name when page loads
document.addEventListener('DOMContentLoaded', function() {
    document.getElementById('playlistName').focus();
});

// Character counter for playlist name
document.getElementById('playlistName').addEventListener('input', function() {
    const maxLength = 100;
    const currentLength = this.value.length;
    const remaining = maxLength - currentLength;
    
    // You can add a character counter display here if needed
    if (remaining < 20) {
        this.style.borderColor = remaining < 0 ? '#dc3545' : '#ffc107';
    } else {
        this.style.borderColor = '#28a745';
    }
});
</script>