<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div class="container my-5">
  <div class="mb-4 text-center">
    <h1>Create New Playlist</h1>
    <p class="text-muted">Build your perfect music collection</p>
  </div>

  <div class="card shadow-sm">
    <div class="card-body">
      <form id="createPlaylistForm"
            action="${pageContext.request.contextPath}/user/playlist/create"
            method="post">

        <!-- Playlist Name -->
        <div class="mb-3">
          <label for="playlistName" class="form-label">
            <i class="bi bi-music-note-list me-1"></i>Playlist Name *
          </label>
          <input type="text"
                 id="playlistName"
                 name="name"
                 class="form-control"
                 placeholder="Enter playlist name..."
                 required maxlength="100">
          <div class="form-text">Give your playlist a memorable name.</div>
        </div>

        <fieldset class="mb-4">
          <legend class="form-label">
            <i class="bi bi-eye-fill me-1"></i>Privacy Settings
          </legend>
          <div class="d-flex gap-3">
            <div class="form-check">
              <input class="form-check-input"
                     type="radio"
                     name="visibility"
                     id="visibilityPublic"
                     value="public"
                     checked>
              <label class="form-check-label" for="visibilityPublic">
                <i class="bi bi-globe2 me-1"></i>
                <strong>Public</strong>
                <small class="d-block text-muted">Anyone can find and listen</small>
              </label>
            </div>
            <div class="form-check">
              <input class="form-check-input"
                     type="radio"
                     name="visibility"
                     id="visibilityPrivate"
                     value="private">
              <label class="form-check-label" for="visibilityPrivate">
                <i class="bi bi-lock-fill me-1"></i>
                <strong>Private</strong>
                <small class="d-block text-muted">Only you can access</small>
              </label>
            </div>
          </div>
        </fieldset>

        <!-- Form Actions -->
        <div class="d-flex justify-content-end">
          <button type="button"
                  class="btn btn-outline-secondary me-2"
                  onclick="window.history.back()">
            <i class="bi bi-arrow-left me-1"></i>Cancel
          </button>

          <button type="submit"
                  class="btn btn-success"
                  id="createBtn">
            <i class="bi bi-plus-lg me-1"></i>Create Playlist
          </button>
        </div>
      </form>
    </div>
  </div>
</div>
