<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<h2>Listen to Music</h2>
<div class="container mt-4">

    <!-- Music Details -->
    <div class="card p-4 mb-4">
        <h2>${music.title}</h2>
        <img alt="No image" src="${pageContext.request.contextPath}/stream/image/music/${music.musicId}">
        <p><strong>Artist:</strong> ${music.artistId}</p>
        <p><strong>Genre:</strong> ${music.genre}</p>
        <p><strong>Released:</strong> ${music.uploadDate}</p>
        
        <!-- Sample Audio Player -->
        <audio controls class="mt-3 w-100">
            <source src="${pageContext.request.contextPath}/stream/audio/${music.musicId}" type="audio/mpeg">
            Your browser does not support the audio element.
        </audio>

        <c:if test="${not empty music.description}">
            <div class="mt-3">
                <p>${music.description}</p>
            </div>
        </c:if>
    </div>

    <!-- Related Tracks -->
    <c:if test="${not empty relatedTracks}">
        <h4>Similar Tracks</h4>
        <ul class="list-group">
            <c:forEach var="track" items="${relatedTracks}">
                <li class="list-group-item d-flex justify-content-between align-items-center">
                    <span>
                        <strong>${track.title}</strong> by ${track.artistId}
                    </span>
                    <a href="${pageContext.request.contextPath}/music/${track.musicId}" class="btn btn-sm btn-outline-primary">Play</a>
                </li>
            </c:forEach>
        </ul>
    </c:if>

</div>
