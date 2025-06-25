<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="container mt-5">
    <div class="card shadow-sm">
        <div class="card-body">
            <h3 class="card-title mb-4">Edit Music</h3>

            <form action="${pageContext.request.contextPath}/artist/music/edit/${musicId}" method="post" enctype="multipart/form-data">
                
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
                    <input type="file" class="form-control" id="file" name="audio" accept="audio/*">
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
                
                <span class="text-muted">**changes may take a few minutes to appear in search results</span>

                <button type="submit" class="btn btn-primary">Update</button>
            </form>
        </div>
    </div>
</div>

<!-- script -->
<script>
    document.addEventListener('DOMContentLoaded', function() {
        const musicDTOJson = '${musicDTOJson}';

        let musicDTO = null;
        if (musicDTOJson && musicDTOJson !== 'null' && musicDTOJson.trim() !== '') {
            try {
                musicDTO = JSON.parse(musicDTOJson);
            } catch (e) {
                console.error("Error parsing musicDTO JSON:", e);
            }
        }

        if (musicDTO) {
            // Autofill Music Title
            const titleInput = document.getElementById('title');
            if (titleInput && musicDTO.title) {
                titleInput.value = musicDTO.title;
            }

            // Autofill Genre
            const genreSelect = document.getElementById('genre');
            if (genreSelect && musicDTO.genreId) {
                // Ensure the option exists before setting value
                const genreOption = genreSelect.querySelector(`option[value="${musicDTO.genreId}"]`);
                if (genreOption) {
                    genreSelect.value = musicDTO.genreId;
                } else {
                    console.warn(`Genre ID ${musicDTO.genreId} not found in genreList options.`);
                }
            }

            // Autofill Mood
            const moodSelect = document.getElementById('mood');
            if (moodSelect && musicDTO.moodId) {
                // Ensure the option exists before setting value
                const moodOption = moodSelect.querySelector(`option[value="${musicDTO.moodId}"]`);
                if (moodOption) {
                    moodSelect.value = musicDTO.moodId;
                } else {
                    console.warn(`Mood ID ${musicDTO.moodId} not found in moodList options.`);
                }
            }
            
            // Autofill Description
            const descriptionTextarea = document.getElementById('description');
            if (descriptionTextarea && musicDTO.description) {
                descriptionTextarea.value = musicDTO.description;
            }

            // Autofill Premium Content checkbox
            const premiumCheckbox = document.getElementById('premiumContent');
            if (premiumCheckbox) {
                premiumCheckbox.checked = musicDTO.premiumContent;
            }
            
            const visibilitySelect = document.getElementById('visibility');
            if (visibilitySelect && musicDTO.visibility) {
                // Ensure the option exists before setting value
                const visibilityOption = moodSelect.querySelector(`option[value="${musicDTO.visibility.value}"]`);
                if (visibilityOption) {
                	visibilitySelect.value = musicDTO.moodId;
                } else {
                    console.warn(`Visibility Type ${musicDTO.visibility.value} not found in visibilityList options.`);
                }
            }
        }
    });
</script>
