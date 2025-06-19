<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="edit-playlist-container">
    <div class="edit-playlist-header">
        <h1>Edit Playlist</h1>
        <p>Modify your playlist settings</p>
    </div>

    <div class="edit-playlist-content">
        <form id="editPlaylistForm" action="${pageContext.request.contextPath}/user/playlist/${playlist.playlistId}/edit" method="post" class="playlist-form">
            
            <!-- Playlist Name -->
            <div class="form-group">
                <label for="playlistName" class="form-label">
                    <i class="fas fa-music"></i> Playlist Name *
                </label>
                <input type="text" 
                       id="playlistName" 
                       name="name" 
                       class="form-control" 
                       placeholder="Enter playlist name..." 
                       value="${playlist.name}"
                       required 
                       maxlength="100">
                <small class="form-text">Give your playlist a memorable name</small>
            </div>



            <!-- Visibility -->
            <div class="form-group">
                <label class="form-label">
                    <i class="fas fa-eye"></i> Privacy Settings
                </label>
                <div class="visibility-options">
                    <div class="radio-option">
                        <input type="radio" 
                               id="visibilityPublic" 
                               name="visibility" 
                               value="public" 
                               ${playlist.visibility == 'public' ? 'checked' : ''}>
                        <label for="visibilityPublic" class="radio-label">
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
                        <input type="radio" 
                               id="visibilityPrivate" 
                               name="visibility" 
                               value="private"
                               ${playlist.visibility == 'private' ? 'checked' : ''}>
                        <label for="visibilityPrivate" class="radio-label">
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

            <!-- Current Playlist Info -->
            <div class="playlist-info-section">
                <h3><i class="fas fa-info-circle"></i> Playlist Information</h3>
                <div class="playlist-stats">
                    <div class="stat-item">
                        <span class="stat-label">Created:</span>
                        <span class="stat-value">${playlist.createdAt}</span>
                    </div>
                    <div class="stat-item">
                        <span class="stat-label">Songs:</span>
                        <span class="stat-value">${playlist.musicList != null ? playlist.musicList.size() : 0} tracks</span>
                    </div>
                    <div class="stat-item">
                        <span class="stat-label">Current Visibility:</span>
                        <span class="stat-value">
                            <i class="fas ${playlist.visibility == 'public' ? 'fa-globe' : 'fa-lock'}"></i>
                            ${playlist.visibility}
                        </span>
                    </div>
                </div>
            </div>

            <!-- Form Actions -->
            <div class="form-actions">
                <a href="${pageContext.request.contextPath}/user/playlist/${playlist.playlistId}" 
                   class="btn btn-secondary">
                    <i class="fas fa-arrow-left"></i> Cancel
                </a>
                
                <button type="button" 
                        class="btn btn-danger" 
                        onclick="confirmDelete(${playlist.playlistId}, '${playlist.name}')">
                    <i class="fas fa-trash"></i> Delete Playlist
                </button>
                
                <button type="submit" 
                        class="btn btn-primary" 
                        id="saveBtn">
                    <i class="fas fa-save"></i> Save Changes
                </button>
            </div>
        </form>

    </div>
</div>

<style>
.edit-playlist-container {
    max-width: 600px;
    margin: 0 auto;
    padding: 2rem;
}

.edit-playlist-header {
    text-align: center;
    margin-bottom: 2rem;
}

.edit-playlist-header h1 {
    color: #333;
    margin-bottom: 0.5rem;
}

.edit-playlist-header p {
    color: #666;
    font-size: 1.1rem;
}

.playlist-form {
    background: white;
    padding: 2rem;
    border-radius: 12px;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.form-group {
    margin-bottom: 1.5rem;
}

.form-label {
    display: block;
    font-weight: 600;
    color: #333;
    margin-bottom: 0.5rem;
    font-size: 1rem;
}

.form-label i {
    margin-right: 0.5rem;
    color: #667eea;
}

.form-control {
    width: 100%;
    padding: 0.75rem;
    border: 2px solid #e1e5e9;
    border-radius: 8px;
    font-size: 1rem;
    transition: border-color 0.3s;
}

.form-control:focus {
    outline: none;
    border-color: #667eea;
    box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
}

.form-text {
    color: #666;
    font-size: 0.875rem;
    margin-top: 0.25rem;
    display: block;
}

.visibility-options {
    display: flex;
    flex-direction: column;
    gap: 1rem;
}

.radio-option {
    position: relative;
}

.radio-option input[type="radio"] {
    position: absolute;
    opacity: 0;
}

.radio-label {
    display: block;
    padding: 1rem;
    border: 2px solid #e1e5e9;
    border-radius: 8px;
    cursor: pointer;
    transition: all 0.3s;
}

.radio-option input[type="radio"]:checked + .radio-label {
    border-color: #667eea;
    background-color: #f8f9ff;
}

.radio-content {
    display: flex;
    align-items: center;
    gap: 1rem;
}

.radio-content i {
    font-size: 1.25rem;
    color: #667eea;
    width: 24px;
}

.radio-content strong {
    display: block;
    margin-bottom: 0.25rem;
    color: #333;
}

.radio-content p {
    margin: 0;
    color: #666;
    font-size: 0.875rem;
}

.playlist-info-section {
    background: #f8f9fa;
    padding: 1.5rem;
    border-radius: 8px;
    margin-bottom: 1.5rem;
}

.playlist-info-section h3 {
    margin: 0 0 1rem 0;
    color: #333;
    font-size: 1.1rem;
}

.playlist-info-section h3 i {
    margin-right: 0.5rem;
    color: #667eea;
}

.playlist-stats {
    display: flex;
    flex-direction: column;
    gap: 0.75rem;
}

.stat-item {
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.stat-label {
    font-weight: 500;
    color: #555;
}

.stat-value {
    color: #333;
    font-weight: 500;
}

.stat-value i {
    margin-right: 0.25rem;
}

.form-actions {
    display: flex;
    gap: 1rem;
    justify-content: space-between;
    align-items: center;
    margin-top: 2rem;
    flex-wrap: wrap;
}

.btn {
    padding: 0.75rem 1.5rem;
    border: none;
    border-radius: 8px;
    font-size: 1rem;
    font-weight: 500;
    cursor: pointer;
    text-decoration: none;
    display: inline-flex;
    align-items: center;
    gap: 0.5rem;
    transition: all 0.3s;
}

.btn-primary {
    background: #667eea;
    color: white;
}

.btn-primary:hover {
    background: #5a67d8;
    transform: translateY(-2px);
}

.btn-secondary {
    background: #6c757d;
    color: white;
}

.btn-secondary:hover {
    background: #5a6268;
    color: white;
}

.btn-danger {
    background: #dc3545;
    color: white;
}

.btn-danger:hover {
    background: #c82333;
}

.btn:disabled {
    opacity: 0.6;
    cursor: not-allowed;
    transform: none;
}

@media (max-width: 768px) {
    .edit-playlist-container {
        padding: 1rem;
    }
    
    .playlist-form {
        padding: 1.5rem;
    }
    
    .form-actions {
        flex-direction: column;
        align-items: stretch;
    }
    
    .btn {
        justify-content: center;
    }
}
</style>

<script>
document.getElementById('editPlaylistForm').addEventListener('submit', function(e) {
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
    const saveBtn = document.getElementById('saveBtn');
    saveBtn.disabled = true;
    saveBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Saving...';
});

// Character counter for playlist name
document.getElementById('playlistName').addEventListener('input', function() {
    const maxLength = 100;
    const currentLength = this.value.length;
    const remaining = maxLength - currentLength;
    
    if (remaining < 20) {
        this.style.borderColor = remaining < 0 ? '#dc3545' : '#ffc107';
    } else {
        this.style.borderColor = '#28a745';
    }
});

function confirmDelete(playlistId, playlistName) {
    if (confirm('Are you sure you want to delete "' + playlistName + '"? This action cannot be undone.')) {
        // Show loading state
        const deleteBtn = event.target;
        deleteBtn.disabled = true;
        deleteBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Deleting...';
        
        fetch('${pageContext.request.contextPath}/user/playlist/' + playlistId + '/delete', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            }
        }).then(response => {
            if (response.ok) {
                window.location.href = '${pageContext.request.contextPath}/user/library';
            } else {
                alert('Error deleting playlist. Please try again.');
                deleteBtn.disabled = false;
                deleteBtn.innerHTML = '<i class="fas fa-trash"></i> Delete Playlist';
            }
        }).catch(error => {
            alert('Error deleting playlist. Please try again.');
            deleteBtn.disabled = false;
            deleteBtn.innerHTML = '<i class="fas fa-trash"></i> Delete Playlist';
        });
    }
}

// Auto-focus on playlist name when page loads
document.addEventListener('DOMContentLoaded', function() {
    document.getElementById('playlistName').focus();
});
</script>