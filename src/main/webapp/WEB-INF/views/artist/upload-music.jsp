<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="upload-form">
    <h2>Upload New Track</h2>
    
    <form action="${pageContext.request.contextPath}/artist/upload" method="post" enctype="multipart/form-data">
        <div>
            <label for="title">Track Title:</label>
            <input type="text" id="title" name="title" required />
        </div>

        <div>
            <label for="genre">Genre:</label>
            <input type="text" id="genre" name="genre" required />
        </div>

        <div>
            <label for="file">Audio File (MP3 only):</label>
            <input type="file" id="file" name="file" accept=".mp3" required />
        </div>

        <div>
            <label for="description">Description:</label>
            <textarea id="description" name="description" rows="4"></textarea>
        </div>

        <button type="submit">Upload</button>
    </form>
</div>
