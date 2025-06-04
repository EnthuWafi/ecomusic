<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<h2>Listen to Music</h2>
<div class="container mt-4">

    <!-- Music Details -->
    <div class="card p-4 mb-4">
        <h2>${musicDTO.music.title}</h2>
        <img alt="No image" src="${pageContext.request.contextPath}/stream/image/music/${musicDTO.music.musicId}">
        <p><strong>Artist:</strong> ${musicDTO.artistUsername}</p>
        <p><strong>Genre:</strong> ${musicDTO.music.genreName}</p>
        <p><strong>Mood:</strong> ${musicDTO.music.moodName}</p>
        <p><strong>Released:</strong> <fmt:parseDate value="${musicDTO.music.uploadDate}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime" type="both" />
								<fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${parsedDateTime}" />
								</p>
        
        <!-- Sample Audio Player -->
        <audio controls class="mt-3 w-100">
            <source src="${pageContext.request.contextPath}/stream/audio/${musicDTO.music.musicId}" type="audio/mpeg">
        </audio>

        <c:if test="${not empty musicDTO.music.description}">
            <div class="mt-3">
                <p>${musicDTO.music.description}</p>
            </div>
        </c:if>
    </div>


</div>
