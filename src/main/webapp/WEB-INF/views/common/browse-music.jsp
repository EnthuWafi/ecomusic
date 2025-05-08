<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<h2>All Music</h2>

<table border="1">
	<tr>
		<th>Title</th>
		<th>Genre</th>
		<th>Description</th>
		<th>Upload Date</th>
		<th>Premium Content</th>
		<th>Link</th>
	</tr>

	<c:forEach var="music" items="${musicList}">
		<tr>
			<td>${music.title}</td>
			<td>${music.genre}</td>
			<td>${music.description}</td>
			<td><fmt:formatDate value="${music.uploadDate}"
					pattern="yyyy-MM-dd" /></td>
			<td>${music.premiumContent ? 'Yes' : 'No'}</td>
			<td><a
				href="${pageContext.request.contextPath}/music/play/${music.musicId}">Play</a></td>
		</tr>
	</c:forEach>

</table>

<!-- Pagination controls -->
<div>
	<c:if test="${currentPage > 1}">
		<a href="?page=${currentPage - 1}">Previous</a>
	</c:if>

	<c:forEach var="i" begin="1" end="${totalPages}">
		<a href="?page=${i}">${i}</a>
	</c:forEach>

	<c:if test="${currentPage < totalPages}">
		<a href="?page=${currentPage + 1}">Next</a>
	</c:if>
</div>