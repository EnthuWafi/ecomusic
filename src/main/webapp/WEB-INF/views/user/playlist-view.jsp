<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%-- Add data-playlist-id to the container for JavaScript access --%>
<div class="playlist-container" data-playlist-id="${playlist.playlistId}">
    <div class="playlist-header">
        <div class="playlist-cover">
            <c:choose>
                <c:when test="${not empty playlist.musicList and not empty playlist.musicList[0].music and not empty playlist.musicList[0].music.imageUrl}">
                    <img src="${pageContext.request.contextPath}/stream/image/music/${playlist.musicList[0].musicId}?size=thumb" alt="${fn:escapeXml(playlist.name)}" />
                </c:when>
                <c:otherwise>
                    <div class="placeholder-cover">
                        <i class="fas fa-music"></i>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
        
        <div class="playlist-info">
            <h1>${fn:escapeXml(playlist.name)}</h1>
            <div class="playlist-meta">
                <c:if test="${not empty playlist and not empty playlist.visibility}">
                    <span>${playlist.visibility.value == 'public' ? 'Public' : 'Private'}</span>
                </c:if>
                <c:if test="${empty playlist or empty playlist.visibility}">
                    <span>[Visibility Unknown]</span>
                </c:if>
                <span>•</span>
                <span>${fn:length(playlist.musicList)} songs</span>
                <c:if test="${not empty playlist.createdAtDate}">
                    <span>•</span>
                    <span><fmt:formatDate value="${playlist.createdAtDate}" pattern="MMM dd,yyyy"/></span>
                </c:if>
            </div>
        </div>
    </div>

    <div class="songs-section">
        <c:choose>
            <c:when test="${empty playlist.musicList}">
                <div class="empty-playlist">
                    <i class="fas fa-music fa-3x"></i>
                    <h3>No songs in this playlist</h3>
                </div>
            </c:when>
            <c:otherwise>
                <div class="songs-header">
                    <span class="col-drag"></span>
                    <span class="col-index">#</span>
                    <span class="col-title">Title</span>
                    <span class="col-artist">Artist</span>
                    <span class="col-stats">Likes / Plays</span>
                    <span class="col-actions">Actions</span>
                </div>
                
                <div class="songs-list" id="songsList">
                    <c:forEach var="playlistMusic" items="${playlist.musicList}" varStatus="status">
                        <div class="song-row" 
                             data-playlist-music-id="${playlistMusic.musicId}"
                             data-position="${playlistMusic.position}"
                             draggable="true">
                            
                            <div class="col-drag">
                                <i class="fas fa-grip-vertical drag-handle"></i>
                            </div>
                            
                            <div class="col-index">
                                ${status.index + 1}
                            </div>
                            
                            <div class="col-title">
                                <c:if test="${not empty playlistMusic.music and not empty playlistMusic.music.imageUrl}">
                                    <img src="${pageContext.request.contextPath}/stream/image/music/${playlistMusic.musicId}?size=thumb" class="song-thumb" />
                                </c:if>
                                <div class="song-info">
                                    <div class="song-title">
                                        <c:if test="${not empty playlistMusic.music and not empty playlistMusic.music.title}">
                                            ${fn:escapeXml(playlistMusic.music.title)}
                                        </c:if>
                                        <c:if test="${empty playlistMusic.music or empty playlistMusic.music.title}">
                                            [Unknown Title]
                                        </c:if>
                                    </div>
                                    <c:if test="${not empty playlistMusic.music and playlistMusic.music.premiumContent}">
                                        <span class="premium-badge"><i class="fas fa-crown"></i> Premium</span>
                                    </c:if>
                                </div>
                            </div>
                            
                            <div class="col-artist">
                                <c:if test="${not empty playlistMusic.artistUsername}">
                                    ${fn:escapeXml(playlistMusic.artistUsername)}
                                </c:if>
                                <c:if test="${empty playlistMusic.artistUsername}">
                                    [Unknown Artist]
                                </c:if>
                            </div>
                            
                            <div class="col-stats">
                                <c:if test="${not empty playlistMusic.music}">
                                    <span>${playlistMusic.music.likeCount} <i class="fas fa-heart"></i></span>
                                    <span>${playlistMusic.music.totalPlayCount} <i class="fas fa-play"></i></span>
                                </c:if>
                                <c:if test="${empty playlistMusic.music}">
                                    <span>-- <i class="fas fa-heart"></i></span>
                                    <span>-- <i class="fas fa-play"></i></span>
                                </c:if>
                            </div>
                            
                            <div class="col-actions">
                                <button class="delete-btn" onclick="removeSong(${playlist.playlistId}, ${playlistMusic.musicId})">
                                    <i class="fas fa-trash"></i>
                                </button>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<style>
/* CSS remains unchanged */
.playlist-container {
    max-width: 1200px;
    margin: 0 auto;
    padding: 2rem;
}

.playlist-header {
    display: flex;
    gap: 2rem;
    margin-bottom: 3rem;
    align-items: end;
}

.playlist-cover img,
.placeholder-cover {
    width: 200px;
    height: 200px;
    border-radius: 8px;
    object-fit: cover;
}

.placeholder-cover {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    display: flex;
    align-items: center;
    justify-content: center;
    color: white;
    font-size: 4rem;
}

.playlist-info h1 {
    font-size: 3rem;
    font-weight: 900;
    margin: 0 0 1rem 0;
    color: #333;
}

.playlist-meta {
    display: flex;
    gap: 0.5rem;
    color: #666;
    font-size: 0.9rem;
}

.empty-playlist {
    text-align: center;
    padding: 4rem;
    color: #999;
}

.songs-header {
    display: grid;
    grid-template-columns: 30px 50px 1fr 200px 120px 80px 60px;
    gap: 1rem;
    padding: 1rem;
    border-bottom: 1px solid #eee;
    font-weight: 600;
    color: #666;
    font-size: 0.85rem;
    text-transform: uppercase;
}

.songs-list {
    display: flex;
    flex-direction: column;
}

.song-row {
    display: grid;
    grid-template-columns: 30px 50px 1fr 200px 120px 80px 60px;
    gap: 1rem;
    padding: 1rem;
    align-items: center;
    border-bottom: 1px solid #f5f5f5;
    transition: background-color 0.2s;
    cursor: move;
}

.song-row:hover {
    background: #f8f9fa;
}

.song-row.dragging {
    opacity: 0.5;
    background: #e3f2fd;
}

.drag-handle {
    color: #ccc;
    cursor: grab;
}

.drag-handle:active {
    cursor: grabbing;
}

.col-index {
    text-align: center;
    color: #666;
    font-weight: 500;
}

.col-title {
    display: flex;
    align-items: center;
    gap: 0.75rem;
    min-width: 0;
}

.song-thumb {
    width: 40px;
    height: 40px;
    border-radius: 4px;
    object-fit: cover;
    flex-shrink: 0;
}

.song-info {
    min-width: 0;
}

.song-title {
    font-weight: 500;
    color: #333;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
}

.premium-badge {
    color: #ffd700;
    font-size: 0.75rem;
    margin-top: 0.25rem;
}

.col-artist {
    color: #666;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
}

.col-stats {
    display: flex;
    flex-direction: column;
    gap: 0.25rem;
    font-size: 0.8rem;
    color: #666;
}

.likes, .plays {
    display: flex;
    align-items: center;
    gap: 0.25rem;
}

.likes i {
    color: #e91e63;
}

.plays i {
    color: #2196f3;
}

.col-duration {
    text-align: right;
    color: #666;
    font-variant-numeric: tabular-nums;
}

.delete-btn {
    width: 32px;
    height: 32px;
    border: none;
    background: transparent;
    color: #999;
    border-radius: 50%;
    cursor: pointer;
    display: flex;
    align-items: center;
    justify-content: center;
    transition: all 0.2s;
}

.delete-btn:hover {
    background: #fee;
    color: #dc3545;
}

/* Drag and drop styles */
.drop-zone {
    background: #e3f2fd;
    border: 2px dashed #2196f3;
    border-radius: 4px;
}

.drop-preview {
    height: 2px;
    background: #2196f3;
    margin: 0.25rem 0;
    border-radius: 1px;
}

@media (max-width: 768px) {
    .playlist-header {
        flex-direction: column;
        align-items: center;
        text-align: center;
    }
    
    .playlist-cover img,
    .placeholder-cover {
        width: 150px;
        height: 150px;
    }
    
    .playlist-info h1 {
        font-size: 2rem;
    }
    
    .songs-header,
    .song-row {
        grid-template-columns: 30px 40px 1fr 40px;
        gap: 0.5rem;
    }
    
    .col-artist,
    .col-stats,
    .col-duration {
        display: none;
    }
}
</style>

<script>
    // This line is processed by JSP on the server side
    const CONTEXT_PATH = "${pageContext.request.contextPath}";
</script>

<script>
let draggedElement = null; // Will store the native DOM element
let dropPreview = null;   // Will store the native DOM element
let currentPlaylistId;

// Use jQuery's document ready function
$(function() {
    // Get the playlist ID from the data attribute using jQuery
    const $playlistContainer = $('.playlist-container');
    if ($playlistContainer.length) { // Check if element exists
        currentPlaylistId = $playlistContainer.data('playlist-id');
    }

    initializeDragAndDrop();
});

function initializeDragAndDrop() {
    const $songsList = $('#songsList');
    if (!$songsList.length) return; // Check if element exists

    // Use jQuery's .on() for event binding
    // 'this' inside these handlers will refer to the native DOM element
    // For drag/drop events, access native event properties via e.originalEvent
    $songsList.find('.song-row').on({
        'dragstart': handleDragStart,
        'dragend': handleDragEnd,
        'dragover': handleDragOver,
        'drop': handleDrop,
        'dragenter': handleDragEnter,
        'dragleave': handleDragLeave
    });
}

function handleDragStart(e) {
    draggedElement = this; // 'this' is the native DOM element
    $(this).addClass('dragging'); // Use jQuery for class manipulation
    e.originalEvent.dataTransfer.effectAllowed = 'move'; // Access originalEvent for native properties
    e.originalEvent.dataTransfer.setData('text/html', this.innerHTML);
}

function handleDragEnd(e) {
    $(this).removeClass('dragging'); // Use jQuery for class manipulation
    draggedElement = null;
    
    $('.drop-preview').remove(); // Use jQuery for element removal
}

function handleDragOver(e) {
    // Prevent default to allow drop
    if (e.originalEvent.preventDefault) {
        e.originalEvent.preventDefault();
    }
    e.originalEvent.dataTransfer.dropEffect = 'move';
    return false;
}

function handleDragEnter(e) {
    if (this !== draggedElement) {
        showDropPreview(this, e); // Pass native element and jQuery event object
    }
}

function handleDragLeave(e) {
    // e.originalEvent.relatedTarget to correctly check for leaving the element
    if (e.originalEvent.relatedTarget && !this.contains(e.originalEvent.relatedTarget)) {
        removeDropPreview();
    }
}

function handleDrop(e) {
    // Stop propagation for both jQuery and native events
    e.stopPropagation();
    if (e.originalEvent.stopPropagation) {
        e.originalEvent.stopPropagation();
    }
    
    if (draggedElement !== this) {
        // Access data attribute using jQuery's .data()
        const draggedMusicId = $(draggedElement).data('playlist-music-id');

        const $parent = $(this).parent(); // Get parent using jQuery
        const rect = this.getBoundingClientRect(); // Still native for precise position
        const mouseY = e.originalEvent.clientY; // Access native event for clientY
        const elementCenter = rect.top + rect.height / 2;
        
        if (mouseY < elementCenter) {
            $(draggedElement).insertBefore(this);
        } else {
            $(draggedElement).insertAfter(this);
        }

        newPosition = $('#songsList .song-row').index(draggedElement) + 1;

        
        updateSongPosition(currentPlaylistId, draggedMusicId, newPosition);
        
        updatePositionNumbers();
    }
    
    removeDropPreview();
    return false;
}

function showDropPreview(element, e) { // Now accepts element and event 'e'
    removeDropPreview();
    // Create jQuery element and get native DOM element for 'dropPreview'
    dropPreview = $('<div>').addClass('drop-preview')[0]; 
    
    const rect = element.getBoundingClientRect();
    const mouseY = e.originalEvent.clientY; // Use originalEvent for mouse Y coordinate
    const elementCenter = rect.top + rect.height / 2;

    if (mouseY < elementCenter) {
        $(dropPreview).insertBefore(element); // Use jQuery for insertion
    } else {
        $(dropPreview).insertAfter(element); // Use jQuery for insertion
    }
}

function removeDropPreview() {
    if (dropPreview) {
        $(dropPreview).remove(); // Use jQuery for removal
        dropPreview = null;
    }
}

function updatePositionNumbers() {
    // Iterate over all song rows using jQuery's .each()
    $('.song-row').each(function(index) {
        const $row = $(this);
        const $indexCell = $row.find('.col-index'); // Find child element using jQuery
        $indexCell.text(index + 1); // Set text content using jQuery
        $row.data('position', index + 1); // Set data attribute using jQuery's .data()
    });
}

// Updated function to use jQuery.ajax for PUT method
function updateSongPosition(playlistId, musicId, newPosition) {
    console.log(`Updating music ID \${musicId} in playlist \${playlistId} to new position \${newPosition}`);
    $.ajax({
        url: `\${CONTEXT_PATH}/api/playlist/\${playlistId}/music/\${musicId}`,
        type: 'PUT', // HTTP method
        contentType: 'application/json', // Specify content type of the request body
        data: JSON.stringify({ position: newPosition }), // Convert data to JSON string
        success: function(data) {
            console.log('Position updated successfully:', data);
        },
        error: function(jqXHR, textStatus, errorThrown) {
            console.error('Server responded with an error:', jqXHR.status, textStatus, errorThrown);
            alert('Error updating song position. Please try again.');
            location.reload(); // Reload page on error
        }
    });
}

// Updated function to use jQuery.ajax for DELETE method
function removeSong(playlistId, musicId) {
    if (!confirm('Remove this song from the playlist?')) {
        return;
    }
    
    // Find song row using jQuery selector
    const $songRow = $(`[data-playlist-music-id="${musicId}"]`);
    if (!$songRow.length) { // Check if element exists using .length
        console.error('Song row not found for musicId:', musicId);
        return;
    }

    $.ajax({
        url: `\${CONTEXT_PATH}/api/playlist/\${playlistId}/music/\${musicId}`,
        type: 'DELETE', // HTTP method
        contentType: 'application/json', // Can be useful even for DELETE if server expects it
        success: function() {
            // Animate removal using jQuery's .animate()
            $songRow.css({overflow: 'hidden'}) // Hide overflow during height animation
                    .animate({ opacity: 0, height: 0, padding: 0, margin: 0 }, 300, function() {
                        $(this).remove(); // Remove element from DOM after animation
                        updatePositionNumbers(); // Update numbers after removal
                        
                        // Check if playlist is empty using jQuery
                        if ($('.song-row').length === 0) {
                            const $songsSection = $('.songs-section');
                            $songsSection.html(`
                                <div class="empty-playlist">
                                    <i class="fas fa-music fa-3x"></i>
                                    <h3>No songs in this playlist</h3>
                                </div>
                            `);
                        }
                    });
        },
        error: function(jqXHR, textStatus, errorThrown) {
            console.error('Failed to remove song:', jqXHR.status, textStatus, errorThrown);
            let errorMessage = 'Error removing song. Please try again.';
            if (jqXHR.responseText) {
                errorMessage += ' Server message: ' + jqXHR.responseText;
            }
            alert(errorMessage);
        }
    });
}
</script>