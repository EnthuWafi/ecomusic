<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="container mt-5">
    <div class="card shadow-sm">
        <div class="card-body">
            <h3 class="card-title mb-4">Upload New Music</h3>

            <form action="${pageContext.request.contextPath}/artist/music/upload" method="post" enctype="multipart/form-data">
                
                <div class="mb-3">
                    <label for="title" class="form-label">Music Title</label>
                    <input type="text" class="form-control" id="title" name="title" required>
                </div>

                <div class="mb-3">
                    <label for="genre" class="form-label">Genre</label>
                    <select class="form-select" id="genre" name="genreId" required>
                        <option value="" disabled selected>Select a genre</option>
                        <c:forEach var="genre" items="${genreList}">
                            <option value="${genre.genreId}">${genre.name}</option>
                        </c:forEach>
                    </select>
                </div>

                <div class="mb-3">
                    <label for="mood" class="form-label">Mood</label>
                    <select class="form-select" id="mood" name="moodId" required>
                        <option value="" disabled selected>Select a mood</option>
                        <c:forEach var="mood" items="${moodList}">
                            <option value="${mood.moodId}">${mood.name}</option>
                        </c:forEach>
                    </select>
                </div>

                <div class="mb-3">
                    <label for="file" class="form-label">Audio File (Supported: .mp3, .wav, .ogg, .m4a)</label>
                    <input type="file" class="form-control" id="file" name="audio" accept="audio/*" required>
                </div>

                <div class="mb-3">
                    <label for="image" class="form-label">Cover Image (optional)</label>
                    <input type="file" class="form-control" id="image" name="image" accept="image/*">
                </div>

                <div class="mb-3">
                    <label for="description" class="form-label">Description</label>
                    <textarea class="form-control" id="description" name="description" rows="4"></textarea>
                </div>

                <div class="form-check mb-3">
                    <input class="form-check-input" type="checkbox" id="premiumContent" name="premiumContent" value="true">
                    <label class="form-check-label" for="premiumContent">
                        Premium Content
                    </label>
                </div>
                
                <div class="mb-3">
                    <label for="visibility" class="form-label">Visibility</label>
                    <select class="form-select" id="visibility" name="visibility" required>
                        <option value="" disabled selected>Select visibility</option>
                          <option value="public">PUBLIC</option>
                          <option value="private">PRIVATE</option>
                    </select>
                </div>
                

                <button type="submit" class="btn btn-primary">Upload</button>
            </form>
        </div>
    </div>
</div>
